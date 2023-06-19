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
To build a Docker image of the application, first build the application using following command:

```shell
mvn -Dmaven.test.skip package
```

then execute the following command:

```shell
docker build -t item-service .
```

To run the Docker container, execute the following command:

```shell
docker run -p 8080:8080 item-service
```
The application will be accessible at http://localhost:8080.

## RabbitMQ Configuration

The RabbitMQ service with the Management Plugin will start as a container named `rabbitmq`, and ports 5672 and 15672 will be exposed. You can access the RabbitMQ Management UI at `http://localhost:15672` to manage and monitor your RabbitMQ instance.

To start RabbitMQ
```shell
docker compose up rabbitmq
```

To enable message logging in RabbitMQ, follow these steps:

1. Access the RabbitMQ Management UI by opening `http://localhost:15672` in a web browser.
2. Log in with the default credentials (`guest/guest`) or the credentials you specified in the `docker-compose.yaml` file.
3. Once logged in, navigate to the "Admin" tab and click on the "Logging" link.
4. In the "Logging" page, click on the "Add new rule" button.
5. Set the following values for the new rule:
    - Rule name: Provide a name for the rule (e.g., "Message Logging").
    - Log level: Select the desired log level (e.g., "info").
    - Log pattern: Set the pattern to `*.*` to log all messages.
6. Click on the "Add rule" button to save the new logging rule.
7. RabbitMQ will start logging all messages based on the configured rule.

With these changes, RabbitMQ will log every message according to the configured logging rule. You can adjust the log level and pattern as needed to customize the logging behavior.