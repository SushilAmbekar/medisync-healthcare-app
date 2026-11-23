package com.healthcare.appointment_service.controller;

import com.healthcare.appointment_service.SecurityConfig;
import com.healthcare.appointment_service.exception.AppointmentNotFoundException;
import com.healthcare.appointment_service.model.Appointment;
import com.healthcare.appointment_service.service.AppointmentService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AppointmentController.class)
@Import(SecurityConfig.class)
class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AppointmentService appointmentService;

    @Test
    void listsAllAppointments() throws Exception {
        when(appointmentService.getAllAppointments()).thenReturn(List.of(appointment(1L, "Asha", "Booked")));

        mockMvc.perform(get("/api/appointments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].patientName").value("Asha"))
                .andExpect(jsonPath("$[0].appointmentDate").value("2026-12-01"))
                .andExpect(jsonPath("$[0].appointmentTime").value("10:30"));
    }

    @Test
    void createsAnAppointmentAsBooked() throws Exception {
        when(appointmentService.saveAppointment(any(Appointment.class))).thenAnswer(call -> call.getArgument(0));

        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"patientName":"Asha","doctorName":"Dr. Iyer","specialty":"Cardiology",
                                 "appointmentDate":"2026-12-01","appointmentTime":"10:30","reason":"Check-up"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Booked"));

        ArgumentCaptor<Appointment> saved = ArgumentCaptor.forClass(Appointment.class);
        verify(appointmentService).saveAppointment(saved.capture());
        assertThat(saved.getValue().getAppointmentDate()).isEqualTo(LocalDate.of(2026, 12, 1));
        assertThat(saved.getValue().getAppointmentTime()).isEqualTo(LocalTime.of(10, 30));
    }

    @Test
    void defaultsTheTimeToNineOClock() throws Exception {
        when(appointmentService.saveAppointment(any(Appointment.class))).thenAnswer(call -> call.getArgument(0));

        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"patientName\":\"Asha\",\"appointmentDate\":\"2026-12-01\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.appointmentTime").value("09:00"));
    }

    @Test
    void rejectsAnInvalidDateWith400() throws Exception {
        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"patientName\":\"Asha\",\"appointmentDate\":\"2026-13-45\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
        verify(appointmentService, never()).saveAppointment(any());
    }

    @Test
    void returns404WhenUpdatingAnUnknownAppointment() throws Exception {
        when(appointmentService.getAppointmentById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/appointments/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"patientName\":\"Asha\",\"appointmentDate\":\"2026-12-01\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void cancelsAnAppointment() throws Exception {
        when(appointmentService.cancelAppointment(1L)).thenReturn(appointment(1L, "Asha", "Cancelled"));

        mockMvc.perform(put("/api/appointments/1/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Cancelled"));
    }

    @Test
    void returns404WhenCancellingAnUnknownAppointment() throws Exception {
        when(appointmentService.cancelAppointment(99L)).thenThrow(new AppointmentNotFoundException(99L));

        mockMvc.perform(put("/api/appointments/99/cancel"))
                .andExpect(status().isNotFound());
    }

    @Test
    void returns404WhenCompletingAnUnknownAppointment() throws Exception {
        when(appointmentService.markAsCompleted(99L)).thenThrow(new AppointmentNotFoundException(99L));

        mockMvc.perform(put("/api/appointments/99/complete"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deletesAnAppointment() throws Exception {
        mockMvc.perform(delete("/api/appointments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Appointment deleted successfully"));
        verify(appointmentService).deleteAppointment(1L);
    }

    @Test
    void returns404WhenDeletingAnUnknownAppointment() throws Exception {
        doThrow(new AppointmentNotFoundException(99L)).when(appointmentService).deleteAppointment(99L);

        mockMvc.perform(delete("/api/appointments/99"))
                .andExpect(status().isNotFound());
    }

    private static Appointment appointment(Long id, String patientName, String status) {
        Appointment appointment = new Appointment();
        ReflectionTestUtils.setField(appointment, "id", id);
        appointment.setPatientName(patientName);
        appointment.setDoctorName("Dr. Iyer");
        appointment.setSpecialty("Cardiology");
        appointment.setAppointmentDate(LocalDate.of(2026, 12, 1));
        appointment.setAppointmentTime(LocalTime.of(10, 30));
        appointment.setStatus(status);
        return appointment;
    }
}
