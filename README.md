# Spring Boot Web API Project

A complete Spring Boot Web API project with RESTful endpoints, JPA/Hibernate, H2 database, and comprehensive error handling.

## Features

- **Spring Boot 3.2.0** with Java 17
- **Spring Data JPA** for database operations
- **H2 In-Memory Database** for development
- **RESTful API** with CRUD operations
- **Input Validation** using Bean Validation
- **Global Exception Handling**
- **Lombok** for reducing boilerplate code
- **CORS** enabled for cross-origin requests
- **Sample Data** initialization

## Project Structure

```
src/
├── main/
│   ├── java/com/example/springwebapi/
│   │   ├── SpringWebApiApplication.java
│   │   ├── controller/
│   │   │   └── UserController.java
│   │   ├── entity/
│   │   │   └── User.java
│   │   ├── repository/
│   │   │   └── UserRepository.java
│   │   ├── service/
│   │   │   └── UserService.java
│   │   ├── exception/
│   │   │   └── GlobalExceptionHandler.java
│   │   └── config/
│   │       └── DataInitializer.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/com/example/springwebapi/
        └── SpringWebApiApplicationTests.java
```

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

## Getting Started

### 1. Clone or Download the Project

### 2. Build the Project
```bash
mvn clean install
```

### 3. Run the Application
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## API Endpoints

### User Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/users` | Get all users |
| GET | `/api/users/{id}` | Get user by ID |
| GET | `/api/users/email/{email}` | Get user by email |
| POST | `/api/users` | Create a new user |
| PUT | `/api/users/{id}` | Update user by ID |
| DELETE | `/api/users/{id}` | Delete user by ID |

### Sample Requests

#### Create User
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john.doe@example.com"
  }'
```

#### Get All Users
```bash
curl -X GET http://localhost:8080/api/users
```

#### Get User by ID
```bash
curl -X GET http://localhost:8080/api/users/1
```

#### Update User
```bash
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Updated",
    "email": "john.updated@example.com"
  }'
```

#### Delete User
```bash
curl -X DELETE http://localhost:8080/api/users/1
```

## Database

- **H2 In-Memory Database** is used for development
- **H2 Console** is available at `http://localhost:8080/h2-console`
  - JDBC URL: `jdbc:h2:mem:testdb`
  - Username: `sa`
  - Password: `password`

## Validation

The API includes input validation:
- Name is required and cannot be blank
- Email is required, must be valid format, and must be unique

## Error Handling

The application includes comprehensive error handling:
- Validation errors return detailed field-specific messages
- Runtime exceptions return appropriate HTTP status codes
- Generic exceptions are handled gracefully

## Testing

Run the tests using:
```bash
mvn test
```

## Development

### Adding New Entities

1. Create entity class in `src/main/java/com/example/springwebapi/entity/`
2. Create repository interface in `src/main/java/com/example/springwebapi/repository/`
3. Create service class in `src/main/java/com/example/springwebapi/service/`
4. Create controller in `src/main/java/com/example/springwebapi/controller/`

### Configuration

Modify `src/main/resources/application.properties` for:
- Database configuration
- Server settings
- Logging levels
- JPA settings

## Production Deployment

For production deployment:
1. Replace H2 with a production database (MySQL, PostgreSQL, etc.)
2. Update `application.properties` with production database settings
3. Configure proper security measures
4. Set up proper logging
5. Configure environment-specific properties

## License

This project is for educational purposes. 