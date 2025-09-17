package com.healthcare.appointment_service.dto;

public class AppointmentRequest {

    private String patientName;
    private String doctorName;
    private String specialty;
    private String appointmentDate; // "YYYY-MM-DD"
    private String appointmentTime; // "HH:mm"
    private String reason;
    private String status;

    // Getters & Setters
    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }

    public String getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(String appointmentDate) { this.appointmentDate = appointmentDate; }

    public String getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(String appointmentTime) { this.appointmentTime = appointmentTime; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }


    public String getStatus() { return status; }  // ✅ getter
    public void setStatus(String status) { this.status = status; }  // ✅ setter
}
