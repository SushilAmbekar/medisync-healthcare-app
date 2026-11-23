import { Appointment } from '../models/appointment.model';

/**
 * Sample appointments for the live demo. Dates are relative to today, so the demo
 * always shows a mix of past and upcoming appointments.
 */
export function sampleAppointments(today: Date = new Date()): Appointment[] {
  const day = (offset: number): string => {
    const date = new Date(today);
    date.setDate(date.getDate() + offset);
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const dayOfMonth = String(date.getDate()).padStart(2, '0');
    return `${date.getFullYear()}-${month}-${dayOfMonth}`;
  };

  return [
    { id: 1, patientName: 'Aarav Joshi', doctorName: 'Dr. Priya Sharma', specialty: 'Cardiologist, MBBS',
      appointmentDate: day(3), appointmentTime: '10:30', reason: 'Follow-up after ECG', status: 'Booked' },
    { id: 2, patientName: 'Isha Kulkarni', doctorName: 'Dr. Neha Deshmukh', specialty: 'Pediatrician, MBBS',
      appointmentDate: day(2), appointmentTime: '11:00', reason: 'Vaccination for child', status: 'Booked' },
    { id: 3, patientName: 'Rohan Verma', doctorName: 'Dr. Rajesh Kumar', specialty: 'Orthopedic, MS',
      appointmentDate: day(1), appointmentTime: '16:15', reason: 'Knee pain', status: 'Booked' },
    { id: 4, patientName: 'Sneha Patil', doctorName: 'Dr. Amit Patil', specialty: 'Dermatologist, MD',
      appointmentDate: day(0), appointmentTime: '09:45', reason: 'Skin allergy', status: 'Booked' },
    { id: 5, patientName: 'Kabir Mehra', doctorName: 'Dr. Suresh Mahajan', specialty: 'Neurologist, MD',
      appointmentDate: day(-1), appointmentTime: '12:00', reason: 'Frequent headaches', status: 'Completed' },
    { id: 6, patientName: 'Ananya Rao', doctorName: 'Dr. Anjali Mehta', specialty: 'Gynecologist, MD',
      appointmentDate: day(-3), appointmentTime: '15:30', reason: 'Routine check-up', status: 'Completed' },
    { id: 7, patientName: 'Vivaan Shah', doctorName: 'Dr. Vikram Singh', specialty: 'ENT Specialist, MBBS',
      appointmentDate: day(-5), appointmentTime: '10:00', reason: 'Ear infection', status: 'Cancelled' },
    { id: 8, patientName: 'Diya Nair', doctorName: 'Dr. Sneha Kulkar', specialty: 'General Physician, MBBS',
      appointmentDate: day(-8), appointmentTime: '17:00', reason: 'Fever and cold', status: 'Completed' }
  ];
}
