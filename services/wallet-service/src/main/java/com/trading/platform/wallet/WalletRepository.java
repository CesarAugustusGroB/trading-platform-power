package com.trading.platform.wallet;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface WalletRepository extends ReactiveMongoRepository<Wallet, String> {
}
