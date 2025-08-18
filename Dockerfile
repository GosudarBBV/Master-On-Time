# Stage 1: Build the JAR
FROM maven:3.9.2-eclipse-temurin-17 AS builder
WORKDIR /application

# Копіюємо Maven файли та код
COPY pom.xml .
COPY src ./src

# Збираємо JAR з тестами пропущеними
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM openjdk:17-jdk-alpine
WORKDIR /application

# Копіюємо зібраний JAR
COPY --from=builder /application/target/*.jar application.jar

ENTRYPOINT ["java", "-jar", "application.jar"]
EXPOSE 8080