# Observability and Monitoring

This project exposes metrics through Prometheus and visualizes them with Grafana.

## Prometheus

Prometheus is configured via `infrastructure/observability/prometheus/prometheus.yml`.
The configuration scrapes each Spring Boot service on `/actuator/prometheus`.

To enable the endpoint in a service add the following dependency and property to
that service's build:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
</dependency>
```

```properties
management.endpoints.web.exposure.include=prometheus,health,info
```

Custom metrics like `orders_placed_total` or `trades_executed_total` can be
registered using Micrometer:

```java
@Autowired
MeterRegistry meterRegistry;

Counter ordersPlaced = Counter.builder("orders_placed_total")
    .description("Total orders placed")
    .register(meterRegistry);
```

## Grafana

Grafana is provisioned automatically when running `docker-compose up`.
Provisioning files reside in `infrastructure/observability/grafana`.
Dashboards are loaded from the `dashboards` folder and include views for order
flow, system health, latency and trading activity.

Access Grafana at <http://localhost:3000> (default `admin`/`admin`).
