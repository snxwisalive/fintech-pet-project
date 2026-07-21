# Nexus Pay

A REST API for managing users, payment cards, and money transfers. Built as a pet project to explore common fintech backend patterns with Spring Boot.

## Features

- **User management** — create, read, update, and delete users
- **Card management** — issue cards to users, look up card details, and remove cards
- **Transactions** — transfer funds between cards and deposit money onto a card
- **Validation** — request validation with Luhn algorithm checks for 16-digit card numbers
- **API documentation** — interactive Swagger UI powered by springdoc-openapi

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 4.0 |
| Persistence | Spring Data JPA, Hibernate |
| Database | PostgreSQL 16 |
| Mapping | MapStruct |
| API docs | springdoc-openapi 3.x |
| Build | Gradle |
| Tests | JUnit 5, Mockito, MockMvc |

## Prerequisites

- Java 17+
- Docker (for PostgreSQL)
- Gradle wrapper included — no separate Gradle install required

## Getting Started

### 1. Start the database

```bash
docker compose up -d
```

This starts PostgreSQL on port **5433** with:

| Setting | Value |
|---|---|
| Database | `fintech` |
| Username | `fintech` |
| Password | `fintech` |

### 2. Run the application

```bash
./gradlew bootRun
```

The API is available at `http://localhost:8080`.

### 3. Open Swagger UI

Browse and try the API at:

```
http://localhost:8080/swagger-ui/index.html
```

## API Overview

### Users — `/api/users`

| Method | Path | Description |
|---|---|---|
| `GET` | `/api/users` | List all users |
| `GET` | `/api/users/{id}` | Get a user by ID |
| `POST` | `/api/users` | Create a user |
| `PATCH` | `/api/users/{id}` | Update a user |
| `DELETE` | `/api/users/{id}` | Delete a user |
| `GET` | `/api/users/{id}/cards` | List cards belonging to a user |

**Create user example:**

```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Jane",
    "lastName": "Doe",
    "phoneNumber": "+1234567890",
    "password": "securepass123"
  }'
```

### Cards — `/api/cards`

| Method | Path | Description |
|---|---|---|
| `POST` | `/api/cards` | Create a card for a user |
| `GET` | `/api/cards/{id}` | Get a card by ID |
| `DELETE` | `/api/cards/{id}` | Delete a card |

**Create card example:**

```bash
curl -X POST http://localhost:8080/api/cards \
  -H "Content-Type: application/json" \
  -d '{ "userId": "<user-uuid>" }'
```

New cards are generated with a random 16-digit number, a four-year expiration date, and a zero balance.

### Transactions — `/api/transactions`

| Method | Path | Description |
|---|---|---|
| `POST` | `/api/transactions/transfer` | Transfer funds between cards |
| `POST` | `/api/transactions/deposit` | Deposit funds onto a card |

**Transfer example:**

```bash
curl -X POST http://localhost:8080/api/transactions/transfer \
  -H "Content-Type: application/json" \
  -d '{
    "fromCardId": "<sender-card-uuid>",
    "toCardNumber": "4242424242424242",
    "amount": 100.00
  }'
```

**Deposit example:**

```bash
curl -X POST http://localhost:8080/api/transactions/deposit \
  -H "Content-Type: application/json" \
  -d '{
    "toCardNumber": "4242424242424242",
    "amount": 250.00
  }'
```

## Running Tests

```bash
./gradlew test
```

Unit and integration tests use an in-memory H2 database, so PostgreSQL does not need to be running for the test suite.

## Project Structure

```
src/main/java/com/example/fintech/
├── controller/     # REST endpoints
├── service/        # Business logic
├── repository/     # JPA repositories
├── model/          # JPA entities (User, Card)
├── DTO/            # Request/response objects
├── mapper/         # MapStruct mappers
├── validation/     # Custom validators (card number)
└── exception/      # Global error handling
```

## Configuration

Application settings live in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5433/fintech?sslmode=disable
spring.datasource.username=fintech
spring.datasource.password=fintech
spring.jpa.hibernate.ddl-auto=update
```

## Notes

- This is a learning project — authentication and authorization are intentionally disabled.
- Card numbers are validated with the Luhn algorithm but are randomly generated, not tied to a real payment network.
- Passwords are stored in plain text and should not be used as a reference for production security practices.
