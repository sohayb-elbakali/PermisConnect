# PermisConnect Backend

This is the backend of the PermisConnect application, built with Spring Boot. It provides the REST API and business logic for the driving school platform.

## Features
- User authentication and management
- Course and lesson APIs
- Progress tracking
- Notification endpoints
- Integration with frontend (React Native/Expo)

## Getting Started (Development)

1. **Install dependencies and build:**
   ```sh
   mvn clean install
   ```
2. **Run the Spring Boot app:**
   ```sh
   mvn spring-boot:run
   ```
3. **API will be available at:**
   - [http://localhost:8080](http://localhost:8080)

## Running with Docker

1. **Build and start with Docker Compose (from project root):**
   ```sh
   docker-compose up --build
   ```
2. **API will be available at:**
   - [http://localhost:8080](http://localhost:8080)

## Project Structure
- `src/` - Java source code
- `Dockerfile` - For containerizing the backend

## Environment Variables
- Configure database and other settings in `application.properties` or via environment variables as needed.

---
For more details, see the main project README or contact the maintainers.

## Prerequisites

- Java 17 or higher
- Maven
- PostgreSQL 12 or higher

## Setup

1. Create a PostgreSQL database named `autoecole`:
```sql
CREATE DATABASE autoecole;
```

2. Update the database configuration in `src/main/resources/application.properties` if needed:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/autoecole
spring.datasource.username=your_username
spring.datasource.password=your_password
```

3. Build the project:
```bash
mvn clean install
```

4. Run the application:
```bash
mvn spring-boot:run
```

The application will start on port 8080.

## API Documentation

Once the application is running, you can access the Swagger UI documentation at:
```
http://localhost:8080/swagger-ui.html
```

## API Endpoints

### Authentication
- POST `/api/auth/login` - Client login
- GET `/api/auth/validate/{clientId}` - Validate client

### Courses
- GET `/api/cours/upcoming` - Get upcoming courses
- GET `/api/cours/public` - Get public courses
- GET `/api/cours/private/{autoEcoleId}` - Get private courses for an auto-école
- GET `/api/cours/moniteur/{moniteurId}` - Get courses by moniteur

### Tests
- GET `/api/tests` - Get available tests
- GET `/api/tests/{testId}` - Get test details
- POST `/api/tests/{testId}/evaluate` - Evaluate test
- GET `/api/tests/results/{clientId}` - Get client test results

### Reservations
- POST `/api/reservations` - Create reservation
- GET `/api/reservations/client/{clientId}` - Get client reservations
- GET `/api/reservations/upcoming` - Get upcoming reservations
- PUT `/api/reservations/{reservationId}/status` - Update reservation status

### Communications
- POST `/api/communications` - Create communication
- POST `/api/communications/messages` - Send message
- GET `/api/communications/client/{clientId}` - Get client communications
- GET `/api/communications/{communicationId}/messages` - Get communication messages
- PUT `/api/communications/messages/{messageId}/read` - Mark message as read
- PUT `/api/communications/{communicationId}/close` - Close communication

### Diagnostics
- POST `/api/diagnostics/client/{clientId}` - Generate client diagnostic

## Development

The project uses:
- Spring Boot 3.2.3
- Spring Data JPA
- PostgreSQL
- Lombok
- Swagger/OpenAPI

## Testing

Run the tests with:
```bash
mvn test
``` 
