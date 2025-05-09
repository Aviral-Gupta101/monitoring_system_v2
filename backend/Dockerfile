FROM gradle:8.13-jdk21-corretto AS builder

WORKDIR /app

# Only copy files needed for dependency resolution first
COPY build.gradle* settings.gradle*  gradlew gradle/ ./
RUN ./gradlew --no-daemon dependencies || true

# Copy the full source code
COPY . .

# Build the project
RUN ./gradlew build -x test --no-daemon

# --- Runtime stage ---
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose application port (adjust if needed)
EXPOSE 8080

# SOCKET SERVER PORT
EXPOSE 9091

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
