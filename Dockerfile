# Stage 1: Build application with Gradle
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app

# Copy repository sources and wrapper
COPY . .

# Build GUI distribution package
RUN chmod +x ./gradlew && ./gradlew :gui:installDist --no-daemon

# Stage 2: Lightweight runtime image
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy seed datasets and application binary
COPY --from=builder /app/data ./data
COPY --from=builder /app/gui/build/install/gui ./gui

# Environment & Port configuration
ENV PORT=8080
EXPOSE 8080

CMD ["./gui/bin/gui"]
