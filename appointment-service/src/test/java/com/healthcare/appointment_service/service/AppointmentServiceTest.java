package com.healthcare.appointment_service.service;

import com.healthcare.appointment_service.exception.AppointmentNotFoundException;
import com.healthcare.appointment_service.model.Appointment;
import com.healthcare.appointment_service.repository.AppointmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository repository;

    @InjectMocks
    private AppointmentService service;

    @Test
    void listsTheNewestAppointmentsFirstAndUndatedOnesLast() {
        when(repository.findAll()).thenReturn(List.of(
                appointment("Asha", LocalDate.of(2026, 1, 5)),
                appointment("Ravi", null),
                appointment("Meera", LocalDate.of(2026, 3, 1))));

        assertThat(service.getAllAppointments())
                .extracting(Appointment::getPatientName)
                .containsExactly("Meera", "Asha", "Ravi");
    }

    @Test
    void searchesByPatientNameIgnoringCase() {
        when(repository.findAll()).thenReturn(List.of(
                appointment("Asha Rao", LocalDate.of(2026, 3, 1)),
                appointment("Ravi", LocalDate.of(2026, 1, 5)),
                appointment("asha", LocalDate.of(2026, 1, 5))));

        assertThat(service.searchAppointmentsByName("ASHA"))
                .extracting(Appointment::getPatientName)
                .containsExactly("asha", "Asha Rao");
    }

    @Test
    void savesANewAppointmentAsBooked() {
        Appointment appointment = appointment("Asha", LocalDate.of(2026, 1, 5));
        appointment.setStatus(null);
        when(repository.save(any(Appointment.class))).thenAnswer(call -> call.getArgument(0));

        assertThat(service.saveAppointment(appointment).getStatus()).isEqualTo("Booked");
    }

    @Test
    void cancelsAnAppointment() {
        Appointment appointment = appointment("Asha", LocalDate.of(2026, 1, 5));
        when(repository.findById(1L)).thenReturn(Optional.of(appointment));
        when(repository.save(appointment)).thenReturn(appointment);

        assertThat(service.cancelAppointment(1L).getStatus()).isEqualTo("Cancelled");
    }

    @Test
    void marksAnAppointmentAsCompleted() {
        Appointment appointment = appointment("Asha", LocalDate.of(2026, 1, 5));
        when(repository.findById(1L)).thenReturn(Optional.of(appointment));
        when(repository.save(appointment)).thenReturn(appointment);

        assertThat(service.markAsCompleted(1L).getStatus()).isEqualTo("Completed");
    }

    @Test
    void throwsNotFoundWhenCancellingAnUnknownAppointment() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.cancelAppointment(99L))
                .isInstanceOf(AppointmentNotFoundException.class)
                .hasMessage("Appointment not found with id 99");
    }

    @Test
    void throwsNotFoundWhenCompletingAnUnknownAppointment() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.markAsCompleted(99L))
                .isInstanceOf(AppointmentNotFoundException.class);
    }

    @Test
    void throwsNotFoundWhenDeletingAnUnknownAppointment() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> service.deleteAppointment(99L))
                .isInstanceOf(AppointmentNotFoundException.class);
        verify(repository, never()).deleteById(99L);
    }

    @Test
    void deletesAnAppointment() {
        when(repository.existsById(1L)).thenReturn(true);

        service.deleteAppointment(1L);

        verify(repository).deleteById(1L);
    }

    private static Appointment appointment(String patientName, LocalDate date) {
        Appointment appointment = new Appointment();
        appointment.setPatientName(patientName);
        appointment.setAppointmentDate(date);
        return appointment;
    }
}
