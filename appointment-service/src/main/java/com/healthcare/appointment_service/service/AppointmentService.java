package com.healthcare.appointment_service.service;

import com.healthcare.appointment_service.model.Appointment;
import com.healthcare.appointment_service.repository.AppointmentRepository;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository repository;

    public AppointmentService(AppointmentRepository repository) {
        this.repository = repository;
    }

    public Page<Appointment> getAppointmentsByPage(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Appointment saveAppointment(Appointment appointment) {
        if (appointment.getStatus() == null || appointment.getStatus().isEmpty()) {
            appointment.setStatus("Booked");
        }
        return repository.save(appointment);
    }

    public List<Appointment> getAllAppointments() {
        return repository.findAll()
                .stream()
                .sorted((a, b) -> {
                    if (a.getAppointmentDate() == null) return 1;
                    if (b.getAppointmentDate() == null) return -1;
                    return b.getAppointmentDate().compareTo(a.getAppointmentDate());
                })
                .toList();
    }

    public java.util.Optional<Appointment> getAppointmentById(Long id) {
        return repository.findById(id);
    }

    public void deleteAppointment(Long id) {
        repository.deleteById(id);
    }

    public List<Appointment> searchAppointmentsByName(String name) {
        String lowerName = name.toLowerCase();
        return repository.findAll().stream()
                .filter(appt -> appt.getPatientName() != null &&
                        appt.getPatientName().toLowerCase().contains(lowerName))
                .sorted((a, b) -> {
                    if (a.getAppointmentDate() == null) return 1;
                    if (b.getAppointmentDate() == null) return -1;
                    return a.getAppointmentDate().compareTo(b.getAppointmentDate());
                })
                .toList();
    }

    public Appointment cancelAppointment(Long id) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setStatus("Cancelled");
                    return repository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Appointment not found with id " + id));
    }

    // ✅ New: Mark appointment as completed
    public Appointment markAsCompleted(Long id) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setStatus("Completed");
                    return repository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Appointment not found with id " + id));
    }
}
