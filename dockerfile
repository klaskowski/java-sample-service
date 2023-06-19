FROM bellsoft/liberica-openjdk-alpine:18.0.2

WORKDIR /app

COPY target/item-service-*.jar item-service.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "item-service.jar"]