package com.healthcare.doctor_service.repository;

import com.healthcare.doctor_service.model.Doctor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DoctorRepositoryTest {

    @Autowired
    private DoctorRepository doctorRepository;

    @Test
    void savesAndFindsADoctor() {
        Doctor saved = doctorRepository.save(new Doctor("Dr. Iyer", "Cardiology", 12));

        assertThat(saved.getId()).isNotNull();
        assertThat(doctorRepository.findById(saved.getId()))
                .hasValueSatisfying(found -> {
                    assertThat(found.getName()).isEqualTo("Dr. Iyer");
                    assertThat(found.getSpecialty()).isEqualTo("Cardiology");
                    assertThat(found.getExperience()).isEqualTo(12);
                });
    }

    @Test
    void deletesADoctor() {
        Long id = doctorRepository.save(new Doctor("Dr. Khan", "Dermatology", 7)).getId();

        doctorRepository.deleteById(id);

        assertThat(doctorRepository.findById(id)).isEmpty();
    }
}
