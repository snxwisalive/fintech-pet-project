# Nexus Pay

Nexus Pay is a Java-based fintech pet project implementing a modular payments platform that demonstrates core payment workflows, integrations, and secure data handling. This repository provides reference implementations for payment processing, account management, webhooks, simulation tools, and testing utilities designed for learning, prototyping, and demonstration purposes.

> Status: Prototype / Educational

## Key features

- Payment lifecycle: create, authorize, capture, refund
- Pluggable gateway adapters (simulated gateway included)
- Account and transaction models with event logging
- Webhook handling and signature verification examples
- Built-in simulator for test payment flows
- Unit and integration tests with examples
- Secure configuration via environment variables

## Project structure (recommended)

- `api/` – REST API layer exposing endpoints for payments, accounts, and webhooks
- `core/` – Business logic, domain models, and services
- `persistence/` – Database access, repositories, and migrations
- `gateway/` – Payment gateway adapters (stripe-like, simulated)
- `webhook/` – Webhook receivers and signature verification utilities
- `simulation/` – Tools to simulate gateway responses and webhooks
- `cli/` – Command-line utilities for administrative tasks and local simulation
- `tests/` – Integration and end-to-end tests

Note: The exact directory layout in this repository may vary. Adjust paths/commands below to match the layout present in the repo.

## Tech stack

- Language: Java (100%)
- Build tools: Maven or Gradle (instructions below for both)
- Suggested runtime: Java 17+
- Database: PostgreSQL (recommended for production; H2 for local tests)
- Testing: JUnit, Mockito

## Prerequisites

- Java 17 or newer installed
- Maven 3.6+ (if using Maven) or Gradle 7+ (if using Gradle)
- PostgreSQL for local development (or use embedded H2 if configured)
- Git

## Quick start

Clone the repo:

```bash
git clone https://github.com/snxwisalive/fintech-pet-project.git
cd fintech-pet-project
```

If the project uses Maven:

```bash
# Build
mvn clean package -DskipTests

# Run (example: if there's a spring-boot app)
java -jar api/target/*.jar
```

If the project uses Gradle wrapper:

```bash
# Make wrapper executable on Unix
chmod +x ./gradlew

# Build
./gradlew clean build -x test

# Run
java -jar api/build/libs/*.jar
```

If the project is modular with separate modules, run the module that contains the runnable artifact (commonly `api` or `app`).

## Configuration

Configure runtime settings using environment variables or property files. The following variables are commonly used:

- `PORT` – HTTP port the API binds to (default: 8080)
- `DATABASE_URL` – JDBC URL for the primary database (e.g. `jdbc:postgresql://localhost:5432/nexus_pay`)
- `DATABASE_USERNAME` / `DATABASE_PASSWORD`
- `GATEWAY_API_KEY` – API key for a real payment gateway (if integrated)
- `WEBHOOK_SECRET` – Secret to validate incoming gateway webhooks
- `LOG_LEVEL` – Logging level (INFO, DEBUG, WARN)

Example `.env` (for local development):

```env
PORT=8080
DATABASE_URL=jdbc:postgresql://localhost:5432/nexus_pay
DATABASE_USERNAME=nexus
DATABASE_PASSWORD=changeMe
WEBHOOK_SECRET=supersecret
GATEWAY_API_KEY=sk_test_xxx
```

If using Spring Boot, place properties in `src/main/resources/application.yml` or `application.properties` and use profiles for `dev`/`test`/`prod`.

## Database

This project assumes a relational database. For development you can use PostgreSQL or an in-memory H2 DB.

- Run migrations with your chosen migration tool (Flyway, Liquibase) if included.
- Example Flyway command (if flyway configured):

```bash
mvn flyway:migrate
# or
./gradlew flywayMigrate
```

## Running tests

Run unit and integration tests using your build tool:

Maven:

```bash
mvn test
```

Gradle:

```bash
./gradlew test
```

For integration tests that require a database, configure a test database or use Docker-based containers (see Docker section).

## Docker (optional)

You can containerize the application if a Dockerfile is provided. Example commands:

```bash
# Build image
docker build -t nexus-pay:latest .

# Run (example)
docker run -e DATABASE_URL="jdbc:postgresql://host.docker.internal:5432/nexus_pay" -p 8080:8080 nexus-pay:latest
```

For local development, consider a docker-compose.yml that brings up the app and a postgres container.

## API examples

Example: create a payment (JSON request)

POST /payments

```json
{
  "amount": 1999,
  "currency": "USD",
  "source": "tok_visa",
  "capture": false,
  "description": "Subscription charge"
}
```

Example: webhook handling

- Validate signature header against `WEBHOOK_SECRET`
- Parse event type and update payment status accordingly

## Security

- Never commit secrets or API keys. Use environment variables or secret managers.
- Validate and verify webhook signatures.
- Use TLS in production.
- Sanitize and validate all inputs to prevent injection attacks.

## Development tips

- Keep gateway adapters small and isolated to make adding new gateways trivial.
- Add contract tests for gateway adapters to ensure behavior matches expectations.
- Use feature flags for experimental payment flows.
- Implement idempotency keys for payment creation endpoints to avoid duplicate charges.

## Contributing

Contributions are welcome. Typical workflow:

1. Fork the repo.
2. Create a feature branch: `git checkout -b feat/your-feature`
3. Commit changes with clear messages.
4. Open a pull request describing the change.

Include tests for new behavior and update documentation when adding features.

## License

This project is provided as an example/educational work. If you intend to use it beyond learning, add an appropriate license file (e.g., MIT) or consult the repository owner.

## Contact

Repository: https://github.com/snxwisalive/fintech-pet-project

Author: snxwisalive

Questions, suggestions, or improvements — please open an issue or a pull request.
