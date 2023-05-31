# Item Service

This is a Java microservice that manages items from a shop.

## Running the Application

To run the application, execute the following command:

```shell
mvn spring-boot:run
```

The application will be accessible at http://localhost:8080.

## API Documentation
The API documentation is generated using Swagger. You can access it at http://localhost:8080/swagger-ui.html.

## Testing
To run the tests, execute the following command:

```shell
mvn test
```

## Docker
To build a Docker image of the application, execute the following command:

```shell
docker build -t item-service .
```

To run the Docker container, execute the following command:

```shell
docker run -p 8080:8080 item-service
```
The application will be accessible at http://localhost:8080.
