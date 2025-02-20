# Build stage
FROM gradle:8.5-jdk17 AS builder

WORKDIR /build

# Copy gradle files
COPY gradlew .
COPY gradle gradle
COPY settings.gradle.kts .
COPY build.gradle.kts .

# Copy source code
COPY src src

# Build application
RUN chmod +x gradlew
RUN ./gradlew clean build -x test

# Run stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Cài đặt curl trong runtime container
RUN apk add --no-cache curl

# Add Spring Boot user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy JAR from build stage
COPY --from=builder /build/build/libs/*.jar app.jar

# Configure JVM
ENV JAVA_OPTS="-Xmx512m -Xms256m"

EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]