# Local Setup

This project is a collection of Spring Boot microservices that depend on MongoDB and Kafka. You can run the infrastructure with Docker Compose or use locally installed services.

## Using Docker Compose

1. Install Docker and Docker Compose.
2. Start the infrastructure containers:
   ```bash
   docker-compose up -d
   ```
   MongoDB is available on `localhost:27017` and Kafka on `localhost:9092`.
3. In separate terminals, run each microservice:
   ```bash
   cd services/orderbook-service && mvn spring-boot:run
   cd services/wallet-service && mvn spring-boot:run
   cd services/trade-service && mvn spring-boot:run
   cd services/portfolio-service && mvn spring-boot:run
   ```
   Each service listens on port `8080` by default. Use `-Dspring-boot.run.arguments=--server.port=<port>` to set a custom port when running multiple services simultaneously.

## Without Docker

1. Install MongoDB and Kafka locally so that they are reachable on the default ports `27017` and `9092`.
2. Ensure Zookeeper is running for Kafka.
3. Start the services using Maven as shown above.

The services use the database `trading` and expect a Kafka broker reachable at `localhost:9092`.
