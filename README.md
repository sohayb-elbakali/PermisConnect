# PermisConnect

PermisConnect is a fullstack platform for driving schools, providing tools for students, instructors, and administrators to manage courses, lessons, tests, reservations, and communications.

## Project Structure

```
PermisConnect/
├── backend/   # Spring Boot REST API
├── frontend/  # Expo/React Native app
└── docker-compose.yml  # Orchestration
```

- **backend/**: Java Spring Boot application providing the REST API and business logic.
- **frontend/**: Expo/React Native application for students and instructors.
- **docker-compose.yml**: Orchestrates both services for local development or deployment.

## Features
- User authentication and management
- Course and lesson management
- Progress tracking
- Test and reservation management
- Real-time communications
- Modern mobile/web frontend

## Getting Started

### Prerequisites
- Docker & Docker Compose (recommended for easiest setup)
- Or: Java 17+, Maven, Node.js 18+/20+, npm, PostgreSQL (for manual setup)

### Running with Docker (Recommended)

1. **Open a terminal in the project root:**
   ```sh
   cd C:\Users\PRO\Desktop\PermisConnect\PermisConnect
   ```
2. **Build and start all services:**
   ```sh
   docker-compose up --build
   ```
3. **Access the apps:**
   - Backend API: [http://localhost:8080](http://localhost:8080)
   - Frontend (Expo DevTools): [http://localhost:8081](http://localhost:8081)

### Running Locally (Without Docker)

#### Backend
See [backend/README.md](backend/README.md) for full instructions.
```sh
cd backend
mvn clean install
mvn spring-boot:run
```

#### Frontend
See [frontend/README.md](frontend/README.md) for full instructions.
```sh
cd frontend
npm install
npm start
```

## Environment Variables
- Configure API URLs and database settings as needed in each service.
- See the respective README files for details.

## Documentation
- Backend API docs: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) (when backend is running)
- See [backend/README.md](backend/README.md) and [frontend/README.md](frontend/README.md) for more.

## Contributing
Pull requests and issues are welcome! Please see the individual service READMEs for development and testing instructions.

---
For questions or support, contact the maintainers. 
