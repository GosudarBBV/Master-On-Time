# Builder stage
FROM openjdk:17-jdk-alpine as builder
WORKDIR /application
COPY master.on.time-0.0.1-SNAPSHOT.jar application.jar
RUN java -Djarmode=layertools -jar application.jar extract

# Final stage
FROM openjdk:17-jdk-alpine
WORKDIR /application
COPY --from=builder /application/dependencies/ ./dependencies/
COPY --from=builder /application/spring-boot-loader/ ./spring-boot-loader/
COPY --from=builder /application/snapshot-dependencies/ ./snapshot-dependencies/
COPY --from=builder /application/application/ ./application/

ENTRYPOINT ["sh", "-c", "sleep 15 && java -cp 'spring-boot-loader/*:dependencies/*:snapshot-dependencies/*:application/*' org.springframework.boot.loader.JarLauncher"]

EXPOSE 8080