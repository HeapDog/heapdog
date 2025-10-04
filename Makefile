.PHONY: help dev dev-build prod prod-build test clean validate db-shell logs stop

# Default target
help:
	@echo "HeapDog - Available Commands:"
	@echo ""
	@echo "Development:"
	@echo "  make dev          - Start development environment"
	@echo "  make dev-build    - Rebuild and start development environment"
	@echo "  make logs         - View application logs"
	@echo "  make stop         - Stop all services"
	@echo ""
	@echo "Production:"
	@echo "  make prod         - Start production build"
	@echo "  make prod-build   - Rebuild and start production build"
	@echo ""
	@echo "Testing:"
	@echo "  make test         - Run tests in Docker"
	@echo "  make validate     - Validate Docker configuration"
	@echo ""
	@echo "Database:"
	@echo "  make db-shell     - Connect to PostgreSQL shell"
	@echo ""
	@echo "Cleanup:"
	@echo "  make clean        - Stop services and remove volumes"
	@echo "  make clean-all    - Remove everything (containers, volumes, images)"

# Development
dev:
	docker compose up

dev-build:
	docker compose up --build

logs:
	docker compose logs -f app

stop:
	docker compose down

# Production
prod:
	docker compose --profile production up app-prod

prod-build:
	docker compose --profile production up --build app-prod

# Testing
test:
	docker build --target tester -t heapdog-test .

validate:
	@./validate-docker.sh

# Database
db-shell:
	docker compose exec postgres psql -U heapdog -d heapdog

# Cleanup
clean:
	docker compose down -v

clean-all:
	docker compose down -v --rmi all
