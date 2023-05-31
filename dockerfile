FROM openjdk:18-jre-slim

WORKDIR /app

COPY target/item-service.jar item-service.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "item-service.jar"]