package com.healthcare.doctor_service.controller;

import com.healthcare.doctor_service.model.Doctor;
import com.healthcare.doctor_service.service.DoctorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DoctorController.class)
class DoctorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DoctorService doctorService;

    @Test
    void listsAllDoctors() throws Exception {
        when(doctorService.getAllDoctors()).thenReturn(List.of(
                doctor(1L, "Dr. Iyer", "Cardiology", 12),
                doctor(2L, "Dr. Khan", "Dermatology", 7)));

        mockMvc.perform(get("/api/doctors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].specialty").value("Cardiology"))
                .andExpect(jsonPath("$[1].experience").value(7));
    }

    @Test
    void returnsADoctorById() throws Exception {
        when(doctorService.getDoctorById(1L)).thenReturn(doctor(1L, "Dr. Iyer", "Cardiology", 12));

        mockMvc.perform(get("/api/doctors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Dr. Iyer"));
    }

    @Test
    void returns404ForAnUnknownDoctor() throws Exception {
        when(doctorService.getDoctorById(99L)).thenReturn(null);

        mockMvc.perform(get("/api/doctors/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createsADoctor() throws Exception {
        when(doctorService.saveDoctor(any(Doctor.class))).thenReturn(doctor(3L, "Dr. Rao", "Neurology", 15));

        mockMvc.perform(post("/api/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Dr. Rao\",\"specialty\":\"Neurology\",\"experience\":15}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.specialty").value("Neurology"));
    }

    @Test
    void deletesADoctor() throws Exception {
        when(doctorService.getDoctorById(1L)).thenReturn(doctor(1L, "Dr. Iyer", "Cardiology", 12));

        mockMvc.perform(delete("/api/doctors/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Doctor deleted successfully"));
        verify(doctorService).deleteDoctor(1L);
    }

    @Test
    void returns404WhenDeletingAnUnknownDoctor() throws Exception {
        when(doctorService.getDoctorById(99L)).thenReturn(null);

        mockMvc.perform(delete("/api/doctors/99"))
                .andExpect(status().isNotFound());
        verify(doctorService, never()).deleteDoctor(99L);
    }

    private static Doctor doctor(Long id, String name, String specialty, int experience) {
        Doctor doctor = new Doctor(name, specialty, experience);
        doctor.setId(id);
        return doctor;
    }
}
