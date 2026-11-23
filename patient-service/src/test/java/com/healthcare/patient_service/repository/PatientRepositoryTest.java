package com.healthcare.patient_service.repository;

import com.healthcare.patient_service.model.Patient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PatientRepositoryTest {

    @Autowired
    private PatientRepository patientRepository;

    @Test
    void savesAndFindsAPatient() {
        Patient patient = new Patient();
        patient.setName("Asha");
        patient.setAge(34);

        Patient saved = patientRepository.save(patient);

        assertThat(saved.getId()).isNotNull();
        assertThat(patientRepository.findById(saved.getId()))
                .hasValueSatisfying(found -> {
                    assertThat(found.getName()).isEqualTo("Asha");
                    assertThat(found.getAge()).isEqualTo(34);
                });
    }

    @Test
    void deletesAPatient() {
        Patient patient = new Patient();
        patient.setName("Ravi");
        patient.setAge(52);
        Long id = patientRepository.save(patient).getId();

        patientRepository.deleteById(id);

        assertThat(patientRepository.findById(id)).isEmpty();
    }
}
