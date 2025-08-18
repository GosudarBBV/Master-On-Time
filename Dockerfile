# Stage 1: Build the JAR
FROM maven:3.9.2-eclipse-temurin-17 AS builder
WORKDIR /application

# Копіюємо Maven файли та код
COPY pom.xml .
COPY src ./src

# Збираємо JAR з Spring Boot layers
RUN mvn clean package spring-boot:build-image -DskipTests

# Stage 2: Run the application
FROM openjdk:17-jdk-alpine
WORKDIR /application

# Копіюємо зібрані шари Spring Boot
COPY --from=builder /application/target/dependency/ ./dependencies/
COPY --from=builder /application/target/spring-boot-loader/ ./spring-boot-loader/
COPY --from=builder /application/target/snapshot-dependencies/ ./snapshot-dependencies/
COPY --from=builder /application/target/application/ ./application/

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
EXPOSE 8080
