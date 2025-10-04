# Multi-stage Dockerfile for production build and testing

# Build stage
FROM eclipse-temurin:24-jdk-noble AS builder

WORKDIR /build

# Copy gradle wrapper and build files
COPY gradle gradle
COPY gradlew gradlew.bat settings.gradle build.gradle ./

# Download dependencies (cached layer)
RUN ./gradlew dependencies --no-daemon || true

# Copy source code
COPY src src

# Build the application (tests will run in CI/CD or separately)
RUN ./gradlew bootJar --no-daemon -x test

# Test stage (optional - can be run separately)
FROM builder AS tester

RUN ./gradlew test --no-daemon

# Runtime stage
FROM eclipse-temurin:24-jre-noble

WORKDIR /app

# Install curl for healthcheck
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Create non-root user
RUN groupadd -r heapdog && useradd -r -g heapdog heapdog

# Copy the built JAR from builder stage
COPY --from=builder /build/build/libs/*.jar app.jar

# Change ownership to non-root user
RUN chown -R heapdog:heapdog /app

# Switch to non-root user
USER heapdog

# Expose application port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
