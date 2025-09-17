🏥 MediSync Healthcare Management Application

Author: Sushil Ambekar
Role: Java Full Stack Developer
Experience: 2 Years

🌟 Project Overview

MediSync is a full-stack Healthcare Management System designed for hospitals and clinics. Patients can book appointments with doctors, and doctors or admins can manage healthcare records efficiently.

Frontend: Angular + Angular Material

Backend: Spring Boot Microservices

Database: H2 / MySQL

Architecture: Microservices (Patient, Doctor, Appointment)

🔑 Key Features

Patient Management: Add, update, view, and delete patient information.

Doctor Management: Add, update, view, and delete doctor profiles.

Appointment Management: Book, update, and cancel appointments.

RESTful API Integration: Frontend communicates seamlessly with backend services.

Clean and Responsive UI: Modern design built with Angular Material.

Microservices Architecture: Separate services for patients, doctors, and appointments for scalability and maintainability.

🛠 Technology Stack
Layer	Technology/Framework
Frontend	Angular, Angular Material
Backend	Java, Spring Boot
Database	H2 / MySQL
REST APIs	Spring Boot REST Controllers
Version Control	Git & GitHub
Build & Serve	npm, Angular CLI
🗂 Project Structure
medisync-healthcare-app/
│
├─ patient-service/        # Backend microservice for patients
├─ doctor-service/         # Backend microservice for doctors
├─ appointment-service/    # Backend microservice for appointments
└─ medisync-frontend/      # Angular frontend

⚙️ Installation & Running Locally

Clone the repository

git clone https://github.com/SushilAmbekar/medisync-healthcare-app.git
cd medisync-healthcare-app


Run Backend Services

# Patient Service
cd patient-service
mvn spring-boot:run  # or java -jar target/patient-service.jar

# Doctor Service
cd ../doctor-service
mvn spring-boot:run

# Appointment Service
cd ../appointment-service
mvn spring-boot:run


Run Angular Frontend

cd medisync-frontend
npm install
ng serve


Open your browser at http://localhost:4200.

🎥 Live Demo

📹 Watch the working application here:


Replace YOUR_VIDEO_ID with your actual YouTube or Loom video ID.

📸 Screenshots

Patient Dashboard

Appointment Lists


Replace the paths with actual screenshots from your project.

📡 API Endpoints
Service	Endpoint	Method
Patient Service	/api/patients	GET, POST
Patient Service	/api/patients/{id}	GET, PUT, DELETE
Doctor Service	/api/doctors	GET, POST
Doctor Service	/api/doctors/{id}	GET, PUT, DELETE
Appointment Service	/api/appointments	GET, POST
Appointment Service	/api/appointments/{id}	GET, PUT, DELETE
📞 Contact & Connect

Name: Sushil Ambekar

LinkedIn: linkedin.com/in/sushilambekar

GitHub: github.com/SushilAmbekar
