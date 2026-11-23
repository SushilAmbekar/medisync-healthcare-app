package com.healthcare.patient_service.controller;

import com.healthcare.patient_service.model.Patient;
import com.healthcare.patient_service.service.PatientService;
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

@WebMvcTest(PatientController.class)
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PatientService patientService;

    @Test
    void listsAllPatients() throws Exception {
        when(patientService.getAllPatients()).thenReturn(List.of(patient(1L, "Asha", 34), patient(2L, "Ravi", 52)));

        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Asha"))
                .andExpect(jsonPath("$[1].age").value(52));
    }

    @Test
    void returnsAPatientById() throws Exception {
        when(patientService.getPatientById(1L)).thenReturn(patient(1L, "Asha", 34));

        mockMvc.perform(get("/api/patients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Asha"));
    }

    @Test
    void returns404ForAnUnknownPatient() throws Exception {
        when(patientService.getPatientById(99L)).thenReturn(null);

        mockMvc.perform(get("/api/patients/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createsAPatient() throws Exception {
        when(patientService.savePatient(any(Patient.class))).thenReturn(patient(3L, "Meera", 28));

        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Meera\",\"age\":28}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("Meera"));
    }

    @Test
    void deletesAPatient() throws Exception {
        when(patientService.getPatientById(1L)).thenReturn(patient(1L, "Asha", 34));

        mockMvc.perform(delete("/api/patients/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Patient deleted successfully"));
        verify(patientService).deletePatient(1L);
    }

    @Test
    void returns404WhenDeletingAnUnknownPatient() throws Exception {
        when(patientService.getPatientById(99L)).thenReturn(null);

        mockMvc.perform(delete("/api/patients/99"))
                .andExpect(status().isNotFound());
        verify(patientService, never()).deletePatient(99L);
    }

    private static Patient patient(Long id, String name, int age) {
        Patient patient = new Patient();
        patient.setId(id);
        patient.setName(name);
        patient.setAge(age);
        return patient;
    }
}
