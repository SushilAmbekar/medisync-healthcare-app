// src/app/pages/create-appointment/create-appointment.component.ts

import { Component, EventEmitter, Output, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { AppointmentService } from '../../services/appointment.service';
import { AppointmentRequest } from '../../models/appointment.model';

@Component({
  selector: 'app-create-appointment',
  templateUrl: './create-appointment.component.html',
  styleUrls: ['./create-appointment.component.css'],
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatSnackBarModule
  ]
})
export class CreateAppointmentComponent {
  private appointmentService = inject(AppointmentService);
  private snackBar = inject(MatSnackBar);

  @Output() appointmentCreated = new EventEmitter<void>();

  patientName = '';
  doctorName = '';
  appointmentDate: Date | null = null;
  appointmentTime = ''; // "HH:mm" string
  timePeriod: 'AM' | 'PM' | '' = '';
  reason = '';

  doctorOptions = [
    { name: 'Dr. Suresh Mahajan', specialty: 'Neurologist, MD' },
    { name: 'Dr. Priya Sharma', specialty: 'Cardiologist, MBBS' },
    { name: 'Dr. Amit Patil', specialty: 'Dermatologist, MD' },
    { name: 'Dr. Neha Deshmukh', specialty: 'Pediatrician, MBBS' },
    { name: 'Dr. Rajesh Kumar', specialty: 'Orthopedic, MS' },
    { name: 'Dr. Anjali Mehta', specialty: 'Gynecologist, MD' },
    { name: 'Dr. Vikram Singh', specialty: 'ENT Specialist, MBBS' },
    { name: 'Dr. Sneha Kulkar', specialty: 'General Physician, MBBS' }
  ];

  saveAppointment() {
    if (!this.patientName || !this.doctorName || !this.appointmentDate || !this.appointmentTime || !this.reason || !this.timePeriod) {
      this.snackBar.open('Please fill all required fields', 'Close', { duration: 2000 });
      return;
    }

    // Format date as YYYY-MM-DD
    const yyyy = this.appointmentDate.getFullYear();
    const mm = String(this.appointmentDate.getMonth() + 1).padStart(2, '0');
    const dd = String(this.appointmentDate.getDate()).padStart(2, '0');
    const formattedDate = `${yyyy}-${mm}-${dd}`;

    // Convert time to 24-hour format
    let [hours, minutes] = this.appointmentTime.split(':').map(Number);
    if (this.timePeriod === 'PM' && hours < 12) hours += 12;
    if (this.timePeriod === 'AM' && hours === 12) hours = 0;
    const formattedTime = `${String(hours).padStart(2, '0')}:${String(minutes).padStart(2, '0')}`;

    const specialty = this.doctorOptions.find(d => d.name === this.doctorName)?.specialty || '';

    const newAppointment: AppointmentRequest = {
      patientName: this.patientName,
      doctorName: this.doctorName,
      specialty: specialty,
      appointmentDate: formattedDate,
      appointmentTime: formattedTime,
      reason: this.reason,
      status: 'Booked'
    };

    this.appointmentService.createAppointment(newAppointment).subscribe({
      next: () => {
        this.snackBar.open('Appointment created successfully!', 'Close', { duration: 2000 });
        this.resetForm();
        this.appointmentCreated.emit();
      },
      error: (err) => {
        console.error('Error creating appointment:', err);
        this.snackBar.open('Error creating appointment', 'Close', { duration: 3000 });
      }
    });
  }

  public resetForm() {
    this.patientName = '';
    this.doctorName = '';
    this.appointmentDate = null;
    this.appointmentTime = '';
    this.timePeriod = '';
    this.reason = '';
  }
}
