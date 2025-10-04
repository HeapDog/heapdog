# HeapDog

HeapDog is a Spring Boot application for user authentication and management.

## Features

- User authentication with JWT tokens
- Secure password hashing with BCrypt
- PostgreSQL database integration
- RESTful API endpoints
- Spring Security integration

## Quick Start

### Using Docker (Recommended)

The easiest way to get started is using Docker:

```bash
# Start the development environment
docker compose up
# or using Make
make dev

# Access the application at http://localhost:8080
```

For detailed Docker setup instructions, see [DOCKER.md](DOCKER.md).

For a quick command reference, see [DOCKER-QUICKREF.md](DOCKER-QUICKREF.md) or run `make help`.

### Using VS Code Dev Container

1. Install the "Dev Containers" extension in VS Code
2. Open this project in VS Code
3. Click "Reopen in Container" when prompted
4. The development environment will be automatically configured

### Manual Setup

**Prerequisites:**
- Java 24 JDK
- PostgreSQL 16
- Gradle 8.14+

**Steps:**

1. Set up PostgreSQL database:
   ```bash
   createdb heapdog
   ```

2. Configure environment variables:
   ```bash
   cp .env.example .env
   # Edit .env with your configuration
   ```

3. Build and run:
   ```bash
   ./gradlew bootRun
   ```

## Development

### Running Tests

```bash
./gradlew test
```

### Building for Production

```bash
./gradlew bootJar
```

The JAR file will be created in `build/libs/`.

## API Endpoints

### Authentication

- `POST /auth/signup` - Register a new user
- `POST /auth/signin` - Authenticate and get JWT token

### Protected Endpoints

All other endpoints require JWT authentication via the `Authorization` header:
```
Authorization: Bearer <your-jwt-token>
```

## Configuration

Key configuration properties:

| Property | Description | Default |
|----------|-------------|---------|
| `DB_HOST` | Database host | localhost |
| `DB_PORT` | Database port | 5432 |
| `DB_NAME` | Database name | heapdog |
| `DB_USER` | Database user | heapdog |
| `DB_PASSWORD` | Database password | - |
| `JWT_SECRET` | JWT signing key (min 32 chars) | - |
| `BCRYPT_SALT_ROUNDS` | BCrypt complexity | 10 |

⚠️ **Security Note**: Always use strong, unique values for `JWT_SECRET` and `DB_PASSWORD` in production!

## Docker Support

This project includes complete Docker support:

- **Development**: `Dockerfile.dev` with hot-reload
- **Production**: Multi-stage `Dockerfile` with optimized runtime
- **Orchestration**: `compose.yml` with PostgreSQL
- **Dev Containers**: VS Code devcontainer configuration

See [DOCKER.md](DOCKER.md) for complete Docker documentation.

## Project Structure

```
heapdog/
├── src/
│   ├── main/
│   │   ├── java/io/heapdog/core/
│   │   │   ├── config/          # Security and application config
│   │   │   ├── controller/      # REST controllers
│   │   │   ├── dto/             # Data transfer objects
│   │   │   ├── exception/       # Custom exceptions
│   │   │   ├── model/           # JPA entities
│   │   │   ├── repository/      # Data repositories
│   │   │   ├── security/        # Security components
│   │   │   └── service/         # Business logic
│   │   └── resources/
│   │       └── application.yml  # Application configuration
│   └── test/                    # Test files
├── .devcontainer/               # VS Code dev container config
├── Dockerfile                   # Production Docker image
├── Dockerfile.dev               # Development Docker image
├── compose.yml                  # Docker Compose configuration
└── build.gradle                 # Gradle build configuration
```

## Technology Stack

- **Framework**: Spring Boot 3.5.6
- **Language**: Java 24
- **Security**: Spring Security + JWT (Nimbus JOSE)
- **Database**: PostgreSQL 16 (H2 for testing)
- **ORM**: Spring Data JPA / Hibernate
- **Build Tool**: Gradle 8.14
- **Containerization**: Docker & Docker Compose

## License

[Add your license here]

## Contributing

[Add contributing guidelines here]
