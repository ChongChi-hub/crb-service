# -----------------------
# First stage: build with Gradle
# -----------------------
FROM gradle:8.6.0-jdk21 AS builder

WORKDIR /app

# Copy only build scripts first (for layer caching)
COPY build.gradle* settings.gradle* gradle /app/

# Copy entire project
COPY . /app

# Build the JAR (adjust to `bootJar` if using Spring Boot)
RUN gradle build --no-daemon

# -----------------------
# Final stage: minimal runtime image
# -----------------------
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copy JAR from builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]