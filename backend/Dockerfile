FROM openjdk:20-jdk-slim

WORKDIR /app

COPY build/libs/ediary-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]