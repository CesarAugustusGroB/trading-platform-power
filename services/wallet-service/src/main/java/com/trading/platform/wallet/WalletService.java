package com.trading.platform.wallet;

import java.math.BigDecimal;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

@Service
public class WalletService {

    private final WalletRepository repository;

    public WalletService(WalletRepository repository) {
        this.repository = repository;
    }

    public Mono<Wallet> deposit(String userId, BigDecimal amount) {
        return repository.findById(userId)
                .defaultIfEmpty(new Wallet(userId))
                .flatMap(w -> {
                    w.setBalance(w.getBalance().add(amount));
                    return repository.save(w);
                });
    }

    public Mono<Wallet> withdraw(String userId, BigDecimal amount) {
        return repository.findById(userId)
                .flatMap(w -> {
                    w.setBalance(w.getBalance().subtract(amount));
                    return repository.save(w);
                });
    }

    public Mono<Wallet> reserve(String userId, BigDecimal amount) {
        return repository.findById(userId)
                .flatMap(w -> {
                    w.setBalance(w.getBalance().subtract(amount));
                    w.setReserved(w.getReserved().add(amount));
                    return repository.save(w);
                });
    }

    public Mono<Wallet> release(String userId, BigDecimal amount) {
        return repository.findById(userId)
                .flatMap(w -> {
                    w.setReserved(w.getReserved().subtract(amount));
                    w.setBalance(w.getBalance().add(amount));
                    return repository.save(w);
                });
    }

    @KafkaListener(topics = "order.placed", groupId = "wallet-service")
    public void handleOrderPlaced(Order order) {
        if (order.getType() == OrderType.BUY) {
            BigDecimal required = order.getPrice().multiply(BigDecimal.valueOf(order.getQuantity()));
            reserve(order.getId(), required).subscribe();
        }
    }

    @KafkaListener(topics = "trade.executed", groupId = "wallet-service")
    public void handleTradeExecuted(Trade trade) {
        BigDecimal price = BigDecimal.ZERO; // price unknown in event
        release(trade.getBuyOrderId(), price).subscribe();
        release(trade.getSellOrderId(), BigDecimal.ZERO).subscribe();
    }

    public Mono<Wallet> getWallet(String userId) {
        return repository.findById(userId).defaultIfEmpty(new Wallet(userId));
    }
}
