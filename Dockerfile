FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build/libs/*.jar public-offering.jar

ENTRYPOINT ["java", "-jar", "public-offering.jar"]
