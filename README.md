# 🗂️ Multi-Tenant Task Management System (Spring Boot REST API)

A **multi-tenant task management system** built with **Spring Boot**, designed to support multiple organizations (tenants) while ensuring data isolation and secure access using **JWT-based authentication & authorization**.

---

## 🚀 Features
- **User Authentication & Authorization**
  - Register & login using JWT authentication
  - Role-based access control (Tenant Admin, User)

- **Tenant Management**
  - Create a tenant (organization) with an admin
  - Admin can invite/add users to their tenant

- **Task Management**
  - Create, update, and delete tasks within a tenant
  - Add/remove collaborators to tasks
  - Restrict collaborators to the same tenant
  - Update task status (e.g., `PENDING`, `IN_PROGRESS`, `COMPLETED`)

- **Multi-Tenancy**
  - Users & tasks are isolated per tenant
  - Admin has control over tenant users

- **Security**
  - Stateless authentication with JWT
  - Passwords encrypted with BCrypt
  - Secure endpoints with Spring Security

- **API Documentation**
  - Swagger UI available at:  
    👉 [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

## 🛠️ Tech Stack
- **Backend**: Spring Boot 3, Spring Security, Spring Data JPA
- **Database**: PostgreSQL
- **Authentication**: JWT (JSON Web Token)
- **Documentation**: Swagger / OpenAPI
- **Build Tool**: Maven

---

## 📂 Project Structure
src/main/java/com/tdila/taskmanager
│── config/ # Security configuration
│── controller/ # REST controllers
│── dto/ # Data Transfer Objects
│── entity/ # JPA Entities
│── repository/ # Spring Data Repositories
│── security/ # JWT & Security filters
│── service/ # Service interfaces
│── service/impl/ # Service implementations

---

## ⚡ Getting Started

### 1️. Clone the repository
```bash
https://github.com/TDila/multi-tenant-task-manager-api
cd taskmanager
```

## 2. Configure the database
Update application.properties (or application.yml) with your PostgreSQL details:
spring.datasource.url=jdbc:postgresql://localhost:5432/taskmanager
spring.datasource.username=your_username
spring.datasource.password=your_password

### JWT Secret & Expiration
jwt.secret=your_secret_key
jwt.expiration=3600000

## 3. Postman Collection
You can view and test the API using the published Postman collection:

[Open AI-Powered Resume Analyzer Postman Collection](https://documenter.getpostman.com/view/22820614/2sB3HjNgnT)
