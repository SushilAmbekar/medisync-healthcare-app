package com.healthcare.appointment_service.controller;

import com.healthcare.appointment_service.model.Appointment;
import com.healthcare.appointment_service.service.AppointmentService;
import com.healthcare.appointment_service.dto.AppointmentRequest;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public List<Appointment> getAllAppointments() {
        return appointmentService.getAllAppointments();
    }

    @GetMapping("/page")
    public Page<Appointment> getAppointmentsByPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return appointmentService.getAppointmentsByPage(pageable);
    }

    @GetMapping("/search")
    public List<Appointment> searchAppointments(@RequestParam String name) {
        return appointmentService.searchAppointmentsByName(name);
    }

    @PostMapping
    public Appointment createAppointment(@RequestBody AppointmentRequest request) {
        LocalDate date = LocalDate.parse(request.getAppointmentDate());
        LocalTime time = (request.getAppointmentTime() == null || request.getAppointmentTime().isEmpty())
                ? LocalTime.of(9, 0)
                : LocalTime.parse(request.getAppointmentTime());

        Appointment appointment = new Appointment();
        appointment.setPatientName(request.getPatientName());
        appointment.setDoctorName(request.getDoctorName());
        appointment.setSpecialty(request.getSpecialty());
        appointment.setAppointmentDate(date);
        appointment.setAppointmentTime(time);
        appointment.setReason(request.getReason());
        appointment.setStatus("Booked");

        return appointmentService.saveAppointment(appointment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateAppointment(
            @PathVariable Long id,
            @RequestBody AppointmentRequest request) {

        return appointmentService.getAppointmentById(id)
                .map(existing -> {
                    existing.setPatientName(request.getPatientName());
                    existing.setDoctorName(request.getDoctorName());
                    existing.setSpecialty(request.getSpecialty());
                    existing.setAppointmentDate(LocalDate.parse(request.getAppointmentDate()));
                    existing.setAppointmentTime(
                            request.getAppointmentTime() == null || request.getAppointmentTime().isEmpty()
                                    ? LocalTime.of(9, 0)
                                    : LocalTime.parse(request.getAppointmentTime())
                    );
                    existing.setReason(request.getReason());
                    existing.setStatus(request.getStatus() != null ? request.getStatus() : existing.getStatus());

                    appointmentService.saveAppointment(existing);

                    return ResponseEntity.ok(Map.of(
                            "message", "Appointment updated successfully",
                            "id", existing.getId().toString()
                    ));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.ok(Map.of(
                "message", "Appointment deleted successfully",
                "id", id.toString()
        ));
    }

    @PutMapping("/{id}/cancel")
    public Appointment cancelAppointment(@PathVariable Long id) {
        return appointmentService.cancelAppointment(id);
    }

    // ✅ New: Mark appointment as completed
    @PutMapping("/{id}/complete")
    public Appointment markAsCompleted(@PathVariable Long id) {
        return appointmentService.markAsCompleted(id);
    }
}
