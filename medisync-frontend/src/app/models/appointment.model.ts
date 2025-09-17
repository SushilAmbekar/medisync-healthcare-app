// src/app/models/appointment.model.ts
export interface Appointment {
  id?: number;
  patientName: string;
  doctorName: string;
  specialty?: string;
  appointmentDate: string; // "YYYY-MM-DD"
  appointmentTime: string; // "HH:mm"
  reason?: string;
  status: string;
}

// Optional: interface for creating/updating appointments
export interface AppointmentRequest {
  patientName: string;
  doctorName: string;
  specialty?: string;
  appointmentDate: string; // "YYYY-MM-DD"
  appointmentTime: string; // "HH:mm"
  reason?: string;
  status?: string;
}
