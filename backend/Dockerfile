FROM eclipse-temurin:20-jdk

WORKDIR /app

COPY build/libs/ediary-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]