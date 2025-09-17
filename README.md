MediSync Healthcare Management Application

Author: Sushil Ambekar
Role: Java Full Stack Developer
Experience: 2+ Years

Project Overview

MediSync is a Healthcare Management System that allows patients to book appointments with doctors and manage healthcare records.
This is a full-stack application built with Angular (frontend) and Spring Boot (backend microservices).

Key Features

Patient Management: View, add, update, and delete patient information.

Doctor Management: View, add, update, and delete doctor information.

Appointment Management: Patients can book, update, or cancel appointments with doctors.

Microservices Architecture: Three separate backend services:

Patient Service (localhost:8081)

Doctor Service (localhost:8082)

Appointment Service (localhost:8083)

RESTful APIs: Communicates between Angular frontend and backend services.

Clean UI: Built with Angular Material and responsive design.

Technology Stack
Layer	Technology/Framework
Frontend	Angular, Angular Material
Backend	Java, Spring Boot
Database	H2 / MySQL
REST APIs	Spring Boot REST Controllers
Version Control	Git & GitHub
Build & Serve	npm, Angular CLI
Project Structure
medisync-healthcare-app/
│
├─ patient-service/        # Backend microservice for patients
├─ doctor-service/         # Backend microservice for doctors
├─ appointment-service/    # Backend microservice for appointments
└─ medisync-frontend/      # Angular frontend

Installation & Running Locally

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

Live Demo

📹 Watch the working application here:


Replace YOUR_VIDEO_ID with the YouTube or Loom video ID of your demo.

Screenshots
Create Appointment Dashboard

Appointment Booking

Replace the screenshot paths with actual images from your project.

API Endpoints
Service	Endpoint	Method
Patient Service	/api/patients	GET, POST
Patient Service	/api/patients/{id}	GET, PUT, DELETE
Doctor Service	/api/doctors	GET, POST
Doctor Service	/api/doctors/{id}	GET, PUT, DELETE
Appointment Service	/api/appointments	GET, POST
Appointment Service	/api/appointments/{id}	GET, PUT, DELETE
Author Contact

Name: Sushil Ambekar

LinkedIn: linkedin.com/in/sushilambekar

GitHub: github.com/SushilAmbekar
