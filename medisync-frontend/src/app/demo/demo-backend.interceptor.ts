import { HttpErrorResponse, HttpInterceptorFn, HttpRequest, HttpResponse } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { delay } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { Appointment } from '../models/appointment.model';
import { sampleAppointments } from './sample-appointments';

const STORAGE_KEY = 'medisync-demo-appointments';
const API_PATH = '/api/appointments';
const LATENCY_MS = 200;

/**
 * In demo mode (the GitHub Pages build), answers the appointments API in the browser,
 * so the live demo works without the Spring Boot services. It behaves like
 * appointment-service: newest appointments first, new ones are "Booked" with a default
 * time of 09:00, and unknown ids answer 404. Changes are kept in localStorage.
 * In every other build it passes requests through unchanged.
 */
export const demoBackendInterceptor: HttpInterceptorFn = (req, next) => {
  if (!environment.demoMode || !req.url.includes(API_PATH)) {
    return next(req);
  }
  return handle(req).pipe(delay(LATENCY_MS));
};

function handle(req: HttpRequest<unknown>): Observable<HttpResponse<unknown>> {
  const appointments = load();
  const idMatch = req.url.match(/\/api\/appointments\/(\d+)/);
  const id = idMatch ? Number(idMatch[1]) : null;

  if (req.method === 'GET' && id === null) {
    return ok(sortNewestFirst(appointments));
  }

  if (req.method === 'POST' && id === null) {
    const body = req.body as Partial<Appointment>;
    const created: Appointment = {
      ...body,
      id: Math.max(0, ...appointments.map(a => a.id ?? 0)) + 1,
      appointmentTime: body.appointmentTime || '09:00',
      status: 'Booked'
    } as Appointment;
    save([...appointments, created]);
    return ok(created);
  }

  const index = appointments.findIndex(a => a.id === id);
  if (id !== null && index === -1) {
    return notFound(req.url, id);
  }

  if (req.method === 'PUT' && id !== null) {
    const updated = { ...appointments[index], ...(req.body as Partial<Appointment>), id };
    appointments[index] = updated;
    save(appointments);
    return ok({ message: 'Appointment updated successfully', id: String(id) });
  }

  if (req.method === 'DELETE' && id !== null) {
    save(appointments.filter(a => a.id !== id));
    return ok({ message: 'Appointment deleted successfully', id: String(id) });
  }

  return throwError(() => new HttpErrorResponse({ status: 405, statusText: 'Method Not Allowed', url: req.url }));
}

function sortNewestFirst(appointments: Appointment[]): Appointment[] {
  return [...appointments].sort((a, b) =>
    `${b.appointmentDate} ${b.appointmentTime}`.localeCompare(`${a.appointmentDate} ${a.appointmentTime}`));
}

function ok(body: unknown): Observable<HttpResponse<unknown>> {
  return of(new HttpResponse({ status: 200, body }));
}

function notFound(url: string, id: number): Observable<never> {
  return throwError(() => new HttpErrorResponse({
    status: 404,
    statusText: 'Not Found',
    url,
    error: { message: `Appointment not found with id ${id}` }
  }));
}

function load(): Appointment[] {
  try {
    const stored = localStorage.getItem(STORAGE_KEY);
    if (stored) {
      return JSON.parse(stored) as Appointment[];
    }
  } catch {
    // Storage can be unavailable (private windows, blocked site data): fall back to the samples.
  }
  return sampleAppointments();
}

function save(appointments: Appointment[]): void {
  try {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(appointments));
  } catch {
    // Without storage the demo still works; changes just do not survive a reload.
  }
}
