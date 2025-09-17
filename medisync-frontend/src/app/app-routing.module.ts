import { Routes } from '@angular/router';
import { CreateAppointmentComponent } from './pages/create-appointment/create-appointment.component';
import { AppointmentListComponent } from './pages/appointment-list/appointment-list.component';
import { AppointmentsPageComponent } from './pages/appointments-page/appointments-page.component';

// ✅ Exported so main.ts can import
export const routes: Routes = [
  {
    path: '',
    component: AppointmentsPageComponent,
    children: [
      { path: '', redirectTo: 'appointments', pathMatch: 'full' },
      { path: 'appointments', component: AppointmentListComponent },
      { path: 'create-appointment', component: CreateAppointmentComponent },
    ],
  },
  { path: '**', redirectTo: '' },
];
