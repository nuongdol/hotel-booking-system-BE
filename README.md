# 🏨 Hotel Booking System - Backend

Backend service for a hotel booking system built with **Java Spring Boot**.

The system provides RESTful APIs for user authentication, hotel and room management, room availability, hotel booking, payment processing, and email notification.

A major focus of this project is handling **concurrent room booking** using Redis and distributed locking to reduce the risk of multiple users booking the same room simultaneously.

---

## 📌 Overview

This project simulates the backend of a hotel booking platform where users can:

- Register and log in
- Authenticate using JWT
- Browse hotels and rooms
- Check room availability
- Book rooms for a specific period
- Temporarily hold rooms during the booking/payment process
- Make payments
- Receive email notifications
- Manage booking information

The backend is designed using a layered architecture with Spring Boot, Spring Data JPA, Spring Security, Redis, and MySQL.

---

## 🛠️ Tech Stack

### Backend

- **Java 17**
- **Spring Boot 3**
- **Spring Web**
- **Spring Data JPA**
- **Hibernate**
- **Spring Security 6**
- **JWT**
- **Spring Validation**
- **Spring Mail**
- **MapStruct**
- **Lombok**
- **SpringDoc OpenAPI / Swagger**

### Database & Storage

- **MySQL** - persistent data storage
- **Redis** - temporary room holding and distributed locking
- **Redisson** - Redis-based distributed lock
  
### Development Tools

- Maven
- Git / GitHub
- Postman
- IntelliJ IDEA
- Docker

---

# 🏗️ Architecture

The project follows a layered architecture:

```text
Client
   │
   ▼
Controller
   │
   ▼
Service
   │
   ├──────────────► Redis / Redisson
   │
   ▼
Repository
   │
   ▼
MySQL
