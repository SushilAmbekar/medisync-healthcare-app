import { Component, OnInit, ViewChild, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule, MatTableDataSource } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, MatSortModule, Sort } from '@angular/material/sort';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';
import * as XLSX from 'xlsx';

import { AppointmentService } from '../../services/appointment.service';
import { Appointment, AppointmentRequest } from '../../models/appointment.model';

@Component({
  selector: 'app-appointment-list',
  templateUrl: './appointment-list.component.html',
  styleUrls: ['./appointment-list.component.css'],
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatButtonModule,
    MatSnackBarModule,
    MatPaginatorModule,
    MatSortModule,
    MatFormFieldModule,
    MatInputModule
  ]
})
export class AppointmentListComponent implements OnInit {
  private appointmentService = inject(AppointmentService);
  private snackBar = inject(MatSnackBar);

  dataSource = new MatTableDataSource<Appointment>([]);
  displayedColumns: string[] = [
    'serial',
    'patientName',
    'doctorName',
    'specialty',
    'appointmentDate',
    'appointmentTime',
    'reason',
    'status',
    'actions'
  ];

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  pageSizeOptions: number[] = [5, 10, 20];

  loggedInUsername: string | null = null;
  loggedInRole: string | null = null;

  ngOnInit() {
    // ✅ Get logged-in user info from localStorage
    this.loggedInUsername = localStorage.getItem('username');
    this.loggedInRole = localStorage.getItem('role');

    this.loadAppointments();
  }

  loadAppointments() {
    this.appointmentService.getAppointments().subscribe({
      next: (data) => {
        // ✅ Filter for PATIENT role
        if (this.loggedInRole === 'ROLE_PATIENT' && this.loggedInUsername) {
          data = data.filter(a => a.patientName === this.loggedInUsername);
        }

        this.dataSource = new MatTableDataSource(data);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;

        // Sort accessor for date + time
        this.dataSource.sortingDataAccessor = (item, property) => {
          if (property === 'appointmentDate') {
            return new Date(`${item.appointmentDate}T${item.appointmentTime}`).getTime();
          }
          return (item as any)[property];
        };

        // Filter across multiple fields
        this.dataSource.filterPredicate = (record: Appointment, filter: string) => {
          const term = filter.trim().toLowerCase();
          return (
            (record.patientName || '').toLowerCase().includes(term) ||
            (record.doctorName || '').toLowerCase().includes(term) ||
            (record.reason || '').toLowerCase().includes(term) ||
            (record.specialty || '').toLowerCase().includes(term)
          );
        };

        // ✅ Fix ExpressionChangedAfterItHasBeenCheckedError
        setTimeout(() => {
          if (this.sort) {
            this.sort.active = 'appointmentDate';
            this.sort.direction = 'desc';
            this.sort.sortChange.emit({
              active: this.sort.active,
              direction: this.sort.direction
            } as Sort);
          }
        });
      },
      error: (err) => {
        console.error('Error loading appointments:', err);
        this.snackBar.open('Error loading appointments', 'Close', { duration: 3000 });
      }
    });
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
    if (this.dataSource.paginator) this.dataSource.paginator.firstPage();
  }

  deleteAppointment(appointment: Appointment) {
    if (!appointment.id) return;
    this.appointmentService.deleteAppointment(appointment.id).subscribe({
      next: () => {
        this.snackBar.open('Appointment deleted', 'Close', { duration: 2000 });
        this.loadAppointments();
      },
      error: (err) => {
        console.error('Error deleting appointment:', err);
        this.snackBar.open('Error deleting appointment', 'Close', { duration: 3000 });
      }
    });
  }

  updateStatus(appointment: Appointment, newStatus: string) {
    if (!appointment.id) return;

    const updatedAppointment: AppointmentRequest = {
      patientName: appointment.patientName,
      doctorName: appointment.doctorName,
      specialty: appointment.specialty,
      appointmentDate: appointment.appointmentDate,
      appointmentTime: appointment.appointmentTime,
      reason: appointment.reason,
      status: newStatus
    };

    this.appointmentService.updateAppointment(appointment.id, updatedAppointment).subscribe({
      next: () => {
        this.snackBar.open('Appointment status updated', 'Close', { duration: 2000 });
        this.loadAppointments();
      },
      error: (err) => {
        console.error('Error updating status:', err);
        this.snackBar.open('Error updating status', 'Close', { duration: 3000 });
      }
    });
  }

  downloadPDF() {
    const doc = new jsPDF();
    doc.text('Appointments Report', 14, 15);
    const tableData: (string | number)[][] = this.dataSource.filteredData.map((a, index) => [
      index + 1,
      a.patientName || '',
      a.doctorName || '',
      a.specialty || '',
      a.appointmentDate || '',
      a.appointmentTime || '',
      a.reason || '',
      a.status || ''
    ]);
    autoTable(doc, {
      startY: 20,
      head: [['#', 'Patient', 'Doctor', 'Specialty', 'Date', 'Time', 'Reason', 'Status']],
      body: tableData
    });
    doc.save('appointments.pdf');
  }

  downloadExcel() {
    const worksheet = XLSX.utils.json_to_sheet(
      this.dataSource.filteredData.map((a, index) => ({
        '#': index + 1,
        Patient: a.patientName || '',
        Doctor: a.doctorName || '',
        Specialty: a.specialty || '',
        Date: a.appointmentDate || '',
        Time: a.appointmentTime || '',
        Reason: a.reason || '',
        Status: a.status || ''
      }))
    );
    const workbook = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(workbook, worksheet, 'Appointments');
    XLSX.writeFile(workbook, 'appointments.xlsx');
  }
}
