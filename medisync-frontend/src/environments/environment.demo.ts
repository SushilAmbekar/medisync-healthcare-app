// The GitHub Pages demo: the appointments API is answered in the browser with sample
// data (see app/demo/demo-backend.interceptor.ts), so no backend is needed.
export const environment = {
  production: true,
  demoMode: true,
  appointmentApiUrl: '/api/appointments',
  patientServiceUrl: '/api/patients',
  doctorServiceUrl: '/api/doctors',
  appointmentServiceUrl: '/api/appointments'
};
