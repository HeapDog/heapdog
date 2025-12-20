FROM gradle:9.1.0-jdk25 AS builder

WORKDIR /home/gradle/project

COPY build.gradle settings.gradle ./
COPY src ./src

RUN gradle clean build --no-daemon -x test

FROM eclipse-temurin:25-jre AS runtime

LABEL maintainer="partho.kr@proton.me" \
      version="1.0.0" \
      description="User service for HeapDog"

ENV JAVA_OPTS="" \
    SERVER_PORT=8080 \
    SPRING_PROFILES_ACTIVE="prod"


RUN groupadd -r appgroup && useradd -r -g appgroup appuser

WORKDIR /app
VOLUME /tmp

ARG JAR_FILE=build/libs/*.jar

COPY --from=builder --chown=appuser:appgroup /home/gradle/project/${JAR_FILE} app.jar

EXPOSE ${SERVER_PORT}

HEALTHCHECK --interval=30s --timeout=3s --start-period=30s --retries=3 \
  CMD curl -f http://localhost:${SERVER_PORT}/actuator/health || exit 1

USER appuser

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]