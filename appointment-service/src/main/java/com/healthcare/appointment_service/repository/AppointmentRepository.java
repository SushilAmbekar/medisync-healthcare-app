package com.healthcare.appointment_service.repository;

import com.healthcare.appointment_service.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Search appointments by patient name (partial match, case-insensitive)
    @Query("SELECT a FROM Appointment a WHERE LOWER(a.patientName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Appointment> findByPatientNameContainingIgnoreCase(@Param("name") String name);
}
