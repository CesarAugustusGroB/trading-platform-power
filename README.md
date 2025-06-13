# Trading Platform Power

This repository provides a scaffold for a microservices based trading platform used for training purposes.

## Repository Structure

```
trading-platform/
│
├── README.md
├── docker-compose.yml            # For local MongoDB, Kafka, etc.
├── .github/
│   └── workflows/                # GitHub Actions CI/CD pipelines
│
├── infrastructure/               # Shared Terraform, Helm charts, monitoring
│   ├── terraform/
│   ├── helm/
│   └── observability/
│       ├── prometheus/
│       └── grafana/
│
├── common/                       # Shared libraries (DTOs, configs)
│   ├── common-domain/
│   ├── common-events/
│   └── common-config/
│
├── services/                     # Each microservice in its own subfolder
│   ├── orderbook-service/
│   ├── wallet-service/
│   ├── portfolio-service/
│   ├── trade-service/
│   └── market-feed-service/
│
└── lambdas/                      # AWS Lambda functions
    ├── price-feed-lambda/
    └── fraud-alert-lambda/
```

This skeleton will be expanded with code and infrastructure configurations in later tasks.

## Local development

The `docker-compose.yml` file spins up MongoDB, Zookeeper and Kafka for testing the
services locally. Start the stack with:

```bash
docker-compose up -d
```

MongoDB will be available on `localhost:27017` and Kafka on `localhost:9092`.
