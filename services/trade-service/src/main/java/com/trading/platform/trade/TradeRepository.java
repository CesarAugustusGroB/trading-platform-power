package com.trading.platform.trade;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface TradeRepository extends ReactiveMongoRepository<Trade, String> {
}
