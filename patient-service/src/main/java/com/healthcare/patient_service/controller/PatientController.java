package com.healthcare.patient_service.controller;

import com.healthcare.patient_service.model.Patient;
import com.healthcare.patient_service.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public List<Patient> getAllPatients() {
        return patientService.getAllPatients();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        Patient patient = patientService.getPatientById(id);
        return patient == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(patient);
    }

    @PostMapping
    public Patient createPatient(@RequestBody Patient patient) {
        return patientService.savePatient(patient);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePatient(@PathVariable Long id) {
        if (patientService.getPatientById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        patientService.deletePatient(id);
        return ResponseEntity.ok("Patient deleted successfully");
    }
}
