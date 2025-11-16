````markdown
# PermisConnect

PermisConnect is a comprehensive fullstack platform designed for driving schools (auto-écoles), providing powerful tools for students, instructors, and administrators to manage courses, lessons, practice tests, reservations, payments, and real-time communications.

## Table of Contents

- [Overview](#overview)
- [Project Structure](#project-structure)
- [Key Features](#key-features)
- [Technology Stack](#technology-stack)
- [Getting Started](#getting-started)
- [Running the Application](#running-the-application)
- [Documentation](#documentation)
- [Development](#development)
- [Testing](#testing)
- [Contributing](#contributing)

## Overview

PermisConnect streamlines the management of driving schools by offering an intuitive mobile and web interface for students to access courses, take practice tests, schedule lessons, and communicate with instructors. The platform integrates payment processing, progress tracking, and comprehensive administrative tools.

## Project Structure

```
PermisConnect/
├── backend/              # Spring Boot REST API
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/autoecole/
│   │   │   └── resources/
│   │   └── test/
│   ├── Dockerfile
│   └── pom.xml
├── frontend/             # Expo/React Native Mobile App
│   ├── src/
│   │   ├── app/          # Expo Router screens
│   │   ├── components/   # Reusable UI components
│   │   ├── screens/      # Screen components
│   │   ├── services/     # API services
│   │   └── contexts/     # React contexts
│   ├── Dockerfile
│   └── package.json
└── docker-compose.yml    # Service orchestration
```

- **backend/**: Java Spring Boot application providing REST API, business logic, and data persistence
- **frontend/**: Expo/React Native cross-platform application for iOS, Android, and web
- **docker-compose.yml**: Docker orchestration for simplified development and deployment

## Key Features

### For Students
- 📱 User authentication and registration
- 📚 Access to driving theory courses and materials
- 🎯 Practice tests (tests blancs) with automated evaluation
- 📅 Lesson scheduling and reservation management
- 📊 Progress tracking and performance analytics
- 💳 Integrated payment processing (Stripe)
- 💬 Real-time chat with instructors
- 🔔 Push notifications for important updates
- 🏫 Auto-école selection and management

### For Instructors & Administrators
- 👥 Client and student management
- 📖 Course content management
- 📝 Test creation and evaluation tools
- 📆 Calendar and reservation oversight
- 💬 Communication channel management
- 📈 Student progress monitoring
- 🎓 Diagnostic report generation

### Technical Features
- 🔐 Secure authentication with Spring Security
- 🗄️ PostgreSQL database with JPA/Hibernate
- 🖼️ Cloud media storage (Cloudinary integration)
- 💰 Payment gateway integration (Stripe)
- 📱 Cross-platform mobile support (iOS/Android/Web)
- 📚 API documentation with Swagger/OpenAPI
- 🐳 Docker containerization for easy deployment

## Technology Stack

### Backend
- **Framework**: Spring Boot 3.2.3
- **Language**: Java 17
- **Database**: PostgreSQL
- **ORM**: Spring Data JPA / Hibernate
- **Security**: Spring Security
- **API Documentation**: SpringDoc OpenAPI 3
- **Payment**: Stripe API
- **Cloud Storage**: Cloudinary
- **Build Tool**: Maven
- **Testing**: JUnit, Spring Boot Test

### Frontend
- **Framework**: Expo ~53.0 / React Native 0.79
- **Language**: TypeScript 5.8
- **UI Library**: React 19.0
- **Navigation**: Expo Router 5.0 / React Navigation 7
- **HTTP Client**: Axios
- **State Management**: React Context API
- **Storage**: AsyncStorage
- **Testing**: Jest, React Testing Library

## Getting Started

### Prerequisites

#### Option 1: Docker (Recommended)
- [Docker Desktop](https://www.docker.com/products/docker-desktop/) (includes Docker Compose)

#### Option 2: Manual Setup
- Java 17 or higher
- Maven 3.6+
- Node.js 18+ or 20+
- npm or yarn
- PostgreSQL 12+

### Clone the Repository

```bash
git clone https://github.com/sohayb-elbakali/PermisConnect.git
cd PermisConnect
```

## Running the Application

### Option 1: Running with Docker (Recommended)

1. **Build and start all services:**
   ```bash
   docker-compose up --build
   ```

2. **Access the applications:**
   - Backend API: [http://localhost:8080](http://localhost:8080)
   - API Documentation: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
   - Frontend (Expo DevTools): [http://localhost:8081](http://localhost:8081)

3. **Stop the services:**
   ```bash
   docker-compose down
   ```

### Option 2: Running Locally (Without Docker)

#### Backend Setup

See [backend/README.md](backend/README.md) for detailed instructions.

```bash
cd backend

# Configure PostgreSQL database (see backend README)

# Build and run
mvn clean install
mvn spring-boot:run
```

The backend API will be available at [http://localhost:8080](http://localhost:8080)

#### Frontend Setup

See [frontend/README.md](frontend/README.md) for detailed instructions.

```bash
cd frontend

# Install dependencies
npm install

# Start development server
npm start
```

Scan the QR code with Expo Go app or run on a simulator.

## Documentation

### API Documentation
- **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) (when backend is running)
- **OpenAPI Spec**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

### Additional Resources
- [Backend Documentation](backend/README.md) - Detailed backend setup, API endpoints, and architecture
- [Frontend Documentation](frontend/README.md) - Frontend setup, structure, and development guide

## Development

### Environment Configuration

#### Backend
Configure in `backend/src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/autoecole
spring.datasource.username=your_username
spring.datasource.password=your_password
```

#### Frontend
Configure API URL in `frontend/src/config.ts` or environment variables.

### Code Style
- Backend: Follow standard Java/Spring Boot conventions
- Frontend: ESLint configuration provided (`eslint.config.js`)

## Testing

### Backend Tests
```bash
cd backend
mvn test
```

### Frontend Tests
```bash
cd frontend
npm test
```

## Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

Please ensure:
- Code follows project conventions
- Tests pass successfully
- Documentation is updated as needed

## License

This project is private and proprietary.

## Contact

For questions, support, or contributions, please contact:
- **Repository**: [github.com/sohayb-elbakali/PermisConnect](https://github.com/sohayb-elbakali/PermisConnect)
- **Branch**: develop

---

**PermisConnect** - Empowering driving schools with modern technology 🚗📱

```` 
