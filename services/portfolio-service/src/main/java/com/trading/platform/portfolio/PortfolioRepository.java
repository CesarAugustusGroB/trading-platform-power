package com.trading.platform.portfolio;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface PortfolioRepository extends ReactiveMongoRepository<Portfolio, String> {
}
