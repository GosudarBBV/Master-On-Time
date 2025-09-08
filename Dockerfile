# Stage 1 — build
FROM eclipse-temurin:17-jdk-alpine as builder
WORKDIR application

# копіюємо готовий jar після збірки (mvn clean package)
COPY master.on.time-0.0.1-SNAPSHOT.jar application.jar

# розкладаємо на шари
RUN java -Djarmode=layertools -jar application.jar extract

# Stage 2 — final
FROM eclipse-temurin:17-jdk-alpine
WORKDIR application

# копіюємо всі шари
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/application/ ./

# запуск через JarLauncher (для layered jar)
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]

EXPOSE 8080