# MediSync – Healthcare Management Application

[![CI](https://github.com/SushilAmbekar/medisync-healthcare-app/actions/workflows/ci.yml/badge.svg)](https://github.com/SushilAmbekar/medisync-healthcare-app/actions/workflows/ci.yml)

## 📌 Overview
**MediSync** is a modern **Healthcare Management System** designed to streamline patient-doctor interactions. Patients can easily **book appointments, view healthcare records, and manage consultations** in a secure and user-friendly environment.  

This is a **Java Full Stack application** built with **Spring Boot (Backend)** and **Angular (Frontend)**, featuring a responsive, intuitive interface suitable for desktops and mobile devices.  

**Key Highlights:**  
- Efficient appointment booking system  
- Doctors categorized by specialization for easy search  
- Comprehensive view of upcoming and past appointments  
- Modern, responsive UI for seamless user experience  

---

## 🚀 Features

### Patient-Centric Features
- **Book Appointments:** Select doctor, date, time, and reason for visit  
- **View Appointments:** Track upcoming and past appointments  
- **Search Doctors:** Filter doctors by specialization (e.g., Cardiologist, Dermatologist, Neurologist)  

### UI/UX Features
- **Responsive Design:** Compatible with desktop, tablet, and mobile devices  
- **Modern Interface:** Built with Angular, Bootstrap, and Angular Material  
- **Intuitive Navigation:** Easy access to appointments, doctor search, and profile management  

---

## 🏗️ Technology Stack

| Layer | Technologies |
|-------|-------------|
| Backend | Spring Boot (Java 17), JPA, MySQL |
| Frontend | Angular, TypeScript, Bootstrap, Angular Material |
| Tools & Utilities | Maven, Git, Postman |
| Deployment & Hosting | GitHub |

---

## ⚙️ Configuration

The three backend services read their database settings from environment variables:

| Variable | Default | Description |
|----------|---------|-------------|
| `DB_URL` | `jdbc:mysql://localhost:3306/healthcare_db...` | JDBC URL of the MySQL database |
| `DB_USERNAME` | `root` | Database user |
| `DB_PASSWORD` | none, required | Database password |

For example, to run a service locally:

```bash
export DB_PASSWORD=your-password
./mvnw spring-boot:run
```

## 🐳 Run with Docker

Each backend service has a multi-stage `Dockerfile`, and `docker-compose.yml` starts MySQL and all three services:

```bash
cp .env.example .env      # then set DB_PASSWORD in .env
docker compose up --build
```

| Service | URL |
|---------|-----|
| patient-service | http://localhost:8081/api/patients |
| doctor-service | http://localhost:8082/api/doctors |
| appointment-service | http://localhost:8083/api/appointments |

Then start the frontend with `npm start` in `medisync-frontend`.

## 📸 Screenshots

| Appointment Booking | Appointments List |
|---------------------|------------------|
| ![Create Appointment](https://drive.google.com/uc?export=view&id=1t9lPa255CKijw1zJjfKlxrROfaDNqcQo) | ![Appointment List](https://drive.google.com/uc?export=view&id=1fTy_muFG0gd8T1sB6HmzwNVvnw_1ywa0) |

| Search Bar | Sidebar Navigation |
|------------|------------------|
| ![Search Bar](https://drive.google.com/uc?export=view&id=1d80PN1Doc8nTIWJgiemPYdGifsBFK6Mj) | ![Sidebar](https://drive.google.com/uc?export=view&id=1HqjC7rB4Ah3fV6_kITAWK79u_6_32fcq) |

---

## 🎥 Demo Video  

Experience the **full functionality of MediSync** in this demo:  

[![Watch Demo](https://drive.google.com/uc?export=view&id=YOUR_THUMBNAIL_FILE_ID)](https://drive.google.com/file/d/1nuL8WCh3NSw28heN2BCWUUQyShi9LT2G/view?usp=sharing)  

---

## 💡 Key Takeaways for Recruiters
- **End-to-End Full Stack Development:** Implemented using Spring Boot (REST APIs) and Angular  
- **Real-World Domain Exposure:** Built for healthcare workflows, including appointment management and doctor specialization  
- **Modern Frontend Skills:** Experience with Angular, Bootstrap, Material Design, and responsive UI  
- **Database & API Integration:** Strong understanding of JPA, MySQL, and RESTful API design  
