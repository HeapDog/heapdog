# Docker Setup for HeapDog

This directory contains Docker configurations for both development and production environments.

## Quick Start

### Development Environment

Start the development environment with PostgreSQL database:

```bash
docker compose up
```

This will:
- Start a PostgreSQL database container
- Build and run the HeapDog application in development mode
- Enable hot-reload for code changes
- Expose the application on http://localhost:8080

### Production Build

To test the production build:

```bash
docker compose --profile production up app-prod
```

This will:
- Build the application with tests
- Run the optimized production image
- Expose the application on http://localhost:8081

## VS Code Dev Container

This project includes a dev container configuration for Visual Studio Code.

1. Install the "Dev Containers" extension in VS Code
2. Open the project folder in VS Code
3. Click "Reopen in Container" when prompted (or use Command Palette: "Dev Containers: Reopen in Container")

The dev container will:
- Set up Java 24 environment
- Install necessary VS Code extensions
- Start PostgreSQL database
- Configure the workspace for Spring Boot development

## Files Overview

- **`Dockerfile.dev`**: Development image with Java 24 JDK and development tools
- **`Dockerfile`**: Multi-stage production image with optimized runtime
  - `builder` stage: Compiles the application
  - `tester` stage: Runs tests (optional, use `--target tester`)
  - Final stage: Minimal runtime image with JRE
- **`compose.yml`**: Centralized Docker Compose configuration for all services
- **`.devcontainer/devcontainer.json`**: VS Code dev container configuration
- **`.dockerignore`**: Files to exclude from Docker builds

## Environment Variables

The following environment variables are configured in `compose.yml`:

### Database
- `DB_HOST`: PostgreSQL host (default: postgres)
- `DB_PORT`: PostgreSQL port (default: 5432)
- `DB_NAME`: Database name (default: heapdog)
- `DB_USER`: Database user (default: heapdog)
- `DB_PASSWORD`: Database password (default: heapdog_secret)

### Application
- `JWT_SECRET`: Secret key for JWT token signing (minimum 32 characters)
- `BCRYPT_SALT_ROUNDS`: BCrypt salt rounds for password hashing (default: 10)

**⚠️ Important**: Change the default JWT_SECRET in production!

## Useful Commands

### Development
```bash
# Start services in detached mode
docker compose up -d

# View logs
docker compose logs -f app

# Stop services
docker compose down

# Rebuild services
docker compose up --build

# Access PostgreSQL
docker compose exec postgres psql -U heapdog -d heapdog
```

### Production Testing
```bash
# Build and run production image
docker compose --profile production build app-prod
docker compose --profile production up app-prod

# Build and test the application
docker build --target tester -t heapdog-test .

# Run only database (useful for local development)
docker compose up postgres
```

### Cleanup
```bash
# Stop and remove containers, networks
docker compose down

# Remove volumes (⚠️ deletes database data)
docker compose down -v

# Remove all (containers, networks, volumes, images)
docker compose down -v --rmi all
```

## Health Checks

The production container includes a health check:
- Endpoint: `http://localhost:8080/actuator/health`
- Interval: 30 seconds
- Timeout: 3 seconds
- Retries: 3

Check container health:
```bash
docker compose ps
```

## Volumes

- **`postgres-data`**: Persists PostgreSQL database data
- **`gradle-cache`**: Caches Gradle dependencies for faster builds

## Network

All services run on the `heapdog-network` bridge network, allowing inter-service communication.

## Troubleshooting

### Port already in use
If port 8080 or 5432 is already in use, modify the port mappings in `compose.yml`:
```yaml
ports:
  - "8081:8080"  # Change host port (left side)
```

### Database connection issues
Ensure PostgreSQL is healthy before the app starts:
```bash
docker compose up postgres
# Wait for "database system is ready to accept connections"
docker compose up app
```

### Permission issues
If you encounter permission errors with volumes, check Docker user mapping or run:
```bash
docker compose down -v
docker compose up --build
```
