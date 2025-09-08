FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/master.on.time-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
EXPOSE 8080