package com.healthcare.doctor_service.model;

import jakarta.persistence.*;

@Entity
@Table(name = "doctors")
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String specialty;
    private int experience; // in years

    public Doctor() {}

    public Doctor(String name, String specialty, int experience) {
        this.name = name;
        this.specialty = specialty;
        this.experience = experience;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getSpecialty() { return specialty; }
    public int getExperience() { return experience; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
    public void setExperience(int experience) { this.experience = experience; }
}
