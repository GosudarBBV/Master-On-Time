# Stage 1: Build the JAR
FROM maven:3.9.2-eclipse-temurin-17 AS builder
WORKDIR /application
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM openjdk:17-jdk-alpine
WORKDIR /application
COPY --from=builder /application/target/*.jar application.jar
ENTRYPOINT ["java", "-jar", "application.jar"]
EXPOSE 8080