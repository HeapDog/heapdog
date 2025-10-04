# Quick Reference - HeapDog Docker Commands

## Development

### Start Development Environment
```bash
docker compose up
# or in detached mode
docker compose up -d
```

### View Logs
```bash
# All services
docker compose logs -f

# Just the app
docker compose logs -f app

# Just the database
docker compose logs -f postgres
```

### Stop Services
```bash
docker compose down
```

### Rebuild After Code Changes
```bash
docker compose up --build
```

## Production Testing

### Start Production Build
```bash
docker compose --profile production up app-prod
```

### Build and Run Tests
```bash
docker build --target tester -t heapdog-test .
```

## Database Operations

### Connect to PostgreSQL
```bash
docker compose exec postgres psql -U heapdog -d heapdog
```

### Database Shell Queries
```sql
-- List all tables
\dt

-- List all users
SELECT * FROM heap_dog_user;

-- Exit
\q
```

## VS Code Dev Container

1. Install "Dev Containers" extension
2. `Ctrl+Shift+P` → "Dev Containers: Reopen in Container"
3. Wait for container to build and start

## Troubleshooting

### Port Already in Use
```bash
# Find process using port 8080
lsof -i :8080
# or
netstat -tulpn | grep 8080

# Stop all containers
docker compose down
```

### Clean Everything
```bash
# Remove containers and networks
docker compose down

# Remove containers, networks, and volumes (⚠️ deletes DB data)
docker compose down -v

# Remove everything including images
docker compose down -v --rmi all
```

### Reset Development Environment
```bash
docker compose down -v
docker compose up --build
```

### Check Container Health
```bash
docker compose ps
docker compose exec app ./gradlew test --no-daemon
```

## Useful Docker Commands

```bash
# List running containers
docker compose ps

# Exec into app container
docker compose exec app bash

# Exec into database container
docker compose exec postgres bash

# View resource usage
docker stats

# Prune unused Docker resources
docker system prune -a
```

## Environment Variables

Create `.env` file from template:
```bash
cp .env.example .env
# Edit .env with your values
```

## Validation

Run the validation script:
```bash
./validate-docker.sh
```
