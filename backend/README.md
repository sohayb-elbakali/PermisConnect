````markdown
# PermisConnect Backend

Spring Boot REST API backend for the PermisConnect driving school management platform. This service handles all business logic, data persistence, authentication, payment processing, and integrations.

## Table of Contents

- [Overview](#overview)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [API Documentation](#api-documentation)
- [API Endpoints](#api-endpoints)
- [Database Configuration](#database-configuration)
- [Running the Application](#running-the-application)
- [Testing](#testing)
- [Deployment](#deployment)

## Overview

The PermisConnect backend is a robust Spring Boot application that provides:
- RESTful API for mobile and web clients
- Secure authentication and authorization
- Database management with PostgreSQL
- Payment processing via Stripe
- Cloud media storage with Cloudinary
- Comprehensive API documentation with Swagger

## Technology Stack

- **Framework**: Spring Boot 3.2.3
- **Language**: Java 17
- **Database**: PostgreSQL
- **ORM**: Spring Data JPA / Hibernate
- **Security**: Spring Security
- **API Documentation**: SpringDoc OpenAPI 3.0 (Swagger)
- **Payment Processing**: Stripe Java SDK 24.6.0
- **Cloud Storage**: Cloudinary
- **Validation**: Jakarta Validation API 3.0.2
- **Build Tool**: Maven
- **Testing**: JUnit 5, Spring Boot Test, Mockito

## Project Structure

```
backend/
├── src/
│   ├── main/
│   │   ├── java/com/autoecole/
│   │   │   ├── config/              # Configuration classes
│   │   │   ├── controllers/         # REST controllers
│   │   │   ├── dto/                 # Data Transfer Objects
│   │   │   ├── exception/           # Exception handlers
│   │   │   ├── models/              # Entity models
│   │   │   ├── repositories/        # JPA repositories
│   │   │   ├── services/            # Business logic services
│   │   │   └── AutoEcoleApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-dev.properties
│   │       └── application-prod.properties
│   └── test/
│       └── java/                    # Unit and integration tests
├── target/                          # Build output
├── Dockerfile
├── pom.xml
└── README.md
```

## Getting Started

### Prerequisites

- **Java 17** or higher
- **Maven 3.6+**
- **PostgreSQL 12+** (or use Docker)
- **IDE**: IntelliJ IDEA, Eclipse, or VS Code (recommended)

### Database Setup

1. **Create a PostgreSQL database:**
```sql
CREATE DATABASE autoecole;
```

2. **Configure database connection** in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/autoecole
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### Build and Run

1. **Install dependencies and build:**
```bash
mvn clean install
```

2. **Run the application:**
```bash
mvn spring-boot:run
```

Or specify a profile:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

3. **The API will be available at:**
   - Base URL: [http://localhost:8080](http://localhost:8080)
   - Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## API Documentation

The backend uses SpringDoc OpenAPI 3 for interactive API documentation.

### Access Swagger UI
Once the application is running:
```
http://localhost:8080/swagger-ui.html
```

### OpenAPI Specification
```
http://localhost:8080/v3/api-docs
```

Swagger provides:
- Complete endpoint documentation
- Request/response schemas
- Interactive API testing
- Authentication details

## API Endpoints

### Authentication & Authorization
- `POST /api/auth/login` - Client login
- `POST /api/auth/register` - User registration
- `GET /api/auth/validate/{clientId}` - Validate client credentials

### Auto-École Management
- `GET /api/autoecoles` - List all driving schools
- `GET /api/autoecoles/{id}` - Get auto-école details
- `POST /api/autoecoles` - Create new auto-école
- `PUT /api/autoecoles/{id}` - Update auto-école

### Courses (Cours)
- `GET /api/cours/upcoming` - Get upcoming courses
- `GET /api/cours/public` - Get public courses
- `GET /api/cours/private/{autoEcoleId}` - Get private courses for an auto-école
- `GET /api/cours/moniteur/{moniteurId}` - Get courses by instructor
- `POST /api/cours` - Create new course
- `PUT /api/cours/{id}` - Update course
- `DELETE /api/cours/{id}` - Delete course

### Tests & Evaluations
- `GET /api/tests` - Get available tests
- `GET /api/tests/{testId}` - Get test details
- `POST /api/tests` - Create test
- `POST /api/tests/{testId}/evaluate` - Evaluate test submission
- `GET /api/tests/results/{clientId}` - Get client test results
- `GET /api/tests/statistics/{clientId}` - Get test statistics

### Reservations
- `POST /api/reservations` - Create lesson reservation
- `GET /api/reservations/client/{clientId}` - Get client reservations
- `GET /api/reservations/moniteur/{moniteurId}` - Get instructor reservations
- `GET /api/reservations/upcoming` - Get upcoming reservations
- `PUT /api/reservations/{reservationId}/status` - Update reservation status
- `DELETE /api/reservations/{reservationId}` - Cancel reservation

### Communications & Chat
- `POST /api/communications` - Create communication channel
- `POST /api/communications/messages` - Send message
- `GET /api/communications/client/{clientId}` - Get client communications
- `GET /api/communications/{communicationId}/messages` - Get messages in channel
- `PUT /api/communications/messages/{messageId}/read` - Mark message as read
- `PUT /api/communications/{communicationId}/close` - Close communication

### Clients & Students
- `GET /api/clients` - List all clients
- `GET /api/clients/{id}` - Get client details
- `PUT /api/clients/{id}` - Update client profile
- `GET /api/clients/{id}/progress` - Get client progress

### Diagnostics & Reports
- `POST /api/diagnostics/client/{clientId}` - Generate client diagnostic report
- `GET /api/diagnostics/{diagnosticId}` - Get diagnostic details

### Payments
- `POST /api/payments/create-intent` - Create Stripe payment intent
- `POST /api/payments/confirm` - Confirm payment
- `GET /api/payments/history/{clientId}` - Get payment history

### Media & Files
- `POST /api/media/upload` - Upload media to Cloudinary
- `DELETE /api/media/{publicId}` - Delete media from cloud

## Database Configuration

### Development Profile (`application-dev.properties`)
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/autoecole
spring.datasource.username=postgres
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### Production Profile (`application-prod.properties`)
```properties
spring.datasource.url=${DATABASE_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
```

### Environment Variables
```bash
DATABASE_URL=jdbc:postgresql://localhost:5432/autoecole
DB_USERNAME=postgres
DB_PASSWORD=your_secure_password
STRIPE_SECRET_KEY=sk_test_...
CLOUDINARY_URL=cloudinary://...
JWT_SECRET=your_jwt_secret
```

## Running the Application

### Option 1: Maven (Local Development)
```bash
# Development mode
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Production mode
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

### Option 2: Docker
From the project root:
```bash
docker-compose up backend
```

Or build and run backend only:
```bash
cd backend
docker build -t permisconnect-backend .
docker run -p 8080:8080 permisconnect-backend
```

### Option 3: Standalone JAR
```bash
# Package the application
mvn clean package

# Run the JAR
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

## Testing

### Run All Tests
```bash
mvn test
```

### Run Specific Test Class
```bash
mvn test -Dtest=ClientServiceTest
```

### Run with Coverage
```bash
mvn clean test jacoco:report
```

### Test Structure
- **Unit Tests**: `src/test/java/com/autoecole/services/`
- **Integration Tests**: `src/test/java/com/autoecole/controllers/`
- **Test Reports**: `target/surefire-reports/`

## Development

### Key Dependencies
```xml
<!-- Core -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- Data -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- Security -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<!-- Payment -->
<dependency>
    <groupId>com.stripe</groupId>
    <artifactId>stripe-java</artifactId>
</dependency>
```

### Code Style
- Follow standard Spring Boot conventions
- Use Lombok annotations to reduce boilerplate
- Implement proper exception handling
- Write comprehensive unit tests

### Lombok Annotations
This project uses Lombok. Ensure your IDE has Lombok plugin installed:
- IntelliJ IDEA: Install Lombok plugin and enable annotation processing
- Eclipse: Install Lombok and restart
- VS Code: Install Lombok extension

## Deployment

### Docker Deployment
```bash
# Build image
docker build -t permisconnect-backend:latest .

# Run container
docker run -d \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DATABASE_URL=jdbc:postgresql://db:5432/autoecole \
  --name permisconnect-backend \
  permisconnect-backend:latest
```

### Production Considerations
- Use environment variables for sensitive configuration
- Enable HTTPS/TLS
- Configure CORS properly
- Set up logging and monitoring
- Use connection pooling for database
- Enable Spring Security
- Configure rate limiting

## Troubleshooting

### Common Issues

**Database Connection Error**
```
Ensure PostgreSQL is running and credentials are correct
Check application.properties configuration
```

**Port Already in Use**
```bash
# Change port in application.properties
server.port=8081
```

**Lombok Not Working**
```
Enable annotation processing in IDE settings
Install Lombok plugin
```

## Contributing

1. Follow Spring Boot best practices
2. Write tests for new features
3. Update API documentation
4. Use meaningful commit messages
5. Keep code clean and maintainable

## Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [SpringDoc OpenAPI](https://springdoc.org/)
- [Stripe API Documentation](https://stripe.com/docs/api)

---

For more information, see the [main project README](../README.md) or contact the development team.

```` 
