# 🖥️ Monitoring System

A full-stack monitoring application that allows you to create and manage health checks for ports and websites. Built with modern technologies and production-ready architecture.

---

## 🚀 Tech Stack

- **Backend:** Spring Boot (Java)
- **Frontend:** React (Vite)
- **Database:** MySQL
- **Containerization:** Docker & Docker Compose
- **Real-time Communication:** Socket.IO

---

## 🔐 Key Features

- ✅ JWT-based Authentication & Authorization
- ✅ Multithreaded backend checks for efficient parallel monitoring
- ✅ Real-time updates to the client using WebSockets (via Socket.IO)
- ✅ Dockerized frontend, backend, and database
- ✅ Environment-based configuration

---

## 🔍 Supported Monitor Checks

- **Port Check** – Monitor TCP port availability on remote hosts
- **Website Check (HTTP/HTTPS)** – Monitor response status and uptime of websites

---

## 🧱 Project Structure

```
.
├── backend/          # Spring Boot API (Dockerized)
├── frontend/         # React frontend (Dockerized)
├── docker-compose.yml
└── README.md
```

---

## 🐳 Getting Started (Using Docker Compose)

### 1. Clone the repository

```bash
git https://github.com/Aviral-Gupta101/monitoring_system_v2.git
cd monitoring-system
```

### 2. Start the application

```bash
docker-compose up --build
```

### 3. Access the system

- **Frontend UI:** [http://localhost:5173](http://localhost:5173)
- **Backend API:** [http://localhost:8080](http://localhost:8080)

---

## 🗄️ Environment Variables

You can configure credentials and DB info in a `.env` file or as Docker secrets.

Example `.env`:

```env
SPRING_DATASOURCE_URL=jdbc:mysql://db:3306/monitoring_system
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=toor
```

---

## ⚡ Real-Time Updates

- The backend uses **Socket.IO** to push real-time updates to the frontend whenever a monitor check (e.g., port or website) is executed.
- Clients are automatically notified of check status changes without needing to refresh.

---

## 📦 Production Build (Frontend)

- The React app is built and served using **Nginx** inside the Docker container.
- Exposed via port **5173** for easy access.

---

## 🧪 API Authentication

All secured endpoints require a valid **JWT token** in the `Authorization` header:

```http
Authorization: Bearer <token>
```

Login endpoint returns a JWT on success.

---

