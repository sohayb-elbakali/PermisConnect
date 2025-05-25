# Auto-École Backend

This is the backend application for the Auto-École project, built with Spring Boot.

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