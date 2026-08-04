# Stage 1: Maven Builder with Java 21
FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime Environment with Java 21
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Install Python 3 for ProcessBuilder scripts
RUN apk add --no-cache python3 py3-pip

# Copy compiled JAR from Stage 1
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
