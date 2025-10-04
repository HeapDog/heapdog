#!/bin/bash
# Docker Setup Validation Script

set -e

echo "🐳 HeapDog Docker Setup Validation"
echo "=================================="
echo ""

# Color codes
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to print status
print_status() {
    if [ $1 -eq 0 ]; then
        echo -e "${GREEN}✓${NC} $2"
    else
        echo -e "${RED}✗${NC} $2"
        exit 1
    fi
}

print_info() {
    echo -e "${YELLOW}ℹ${NC} $1"
}

# Check if Docker is installed
print_info "Checking Docker installation..."
docker --version > /dev/null 2>&1
print_status $? "Docker is installed"

# Check if Docker Compose is installed
print_info "Checking Docker Compose installation..."
docker compose version > /dev/null 2>&1
print_status $? "Docker Compose is installed"

# Validate docker-compose.yml
print_info "Validating compose.yml..."
docker compose config --quiet
print_status $? "compose.yml is valid"

# Validate Dockerfile
print_info "Validating Dockerfile..."
docker build -f Dockerfile --target builder -t heapdog-validate . > /dev/null 2>&1
print_status $? "Dockerfile is valid"

# Validate Dockerfile.dev
print_info "Validating Dockerfile.dev..."
if docker build -f Dockerfile.dev -t heapdog-dev-validate --no-cache --quiet . > /dev/null 2>&1; then
    print_status 0 "Dockerfile.dev is valid"
    docker rmi heapdog-dev-validate > /dev/null 2>&1 || true
else
    print_status 1 "Dockerfile.dev validation failed"
fi

# Check devcontainer configuration
print_info "Checking devcontainer configuration..."
if [ -f ".devcontainer/devcontainer.json" ]; then
    print_status 0 "devcontainer.json exists"
else
    print_status 1 "devcontainer.json not found"
fi

echo ""
echo "=================================="
echo -e "${GREEN}✓ All validations passed!${NC}"
echo ""
echo "Next steps:"
echo "  1. Start development environment: docker compose up"
echo "  2. Start production build: docker compose --profile production up app-prod"
echo "  3. Open in VS Code Dev Container: Open folder in VS Code and select 'Reopen in Container'"
