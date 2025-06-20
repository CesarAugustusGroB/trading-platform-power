package com.trading.platform.portfolio;

import java.util.HashMap;
import java.util.Map;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

@Service
public class PortfolioService {

    private final PortfolioRepository repository;

    public PortfolioService(PortfolioRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(topics = "trade.executed", groupId = "portfolio-service")
    public void handleTradeExecuted(Trade trade) {
        // simplistic user handling assuming order ids represent user ids
        updatePosition(trade.getBuyOrderId(), trade.getSymbol(), trade.getQuantity());
        updatePosition(trade.getSellOrderId(), trade.getSymbol(), -trade.getQuantity());
    }

    private void updatePosition(String userId, String symbol, int qty) {
        repository.findById(userId)
                .defaultIfEmpty(new Portfolio(userId))
                .flatMap(p -> {
                    Map<String, Position> map = new HashMap<>();
                    for (Position pos : p.getPositions()) {
                        map.put(pos.getSymbol(), pos);
                    }
                    Position pos = map.getOrDefault(symbol, new Position(symbol, 0));
                    pos.setQuantity(pos.getQuantity() + qty);
                    map.put(symbol, pos);
                    p.setPositions(new java.util.ArrayList<>(map.values()));
                    return repository.save(p);
                })
                .subscribe();
    }

    public Mono<Portfolio> getPortfolio(String userId) {
        return repository.findById(userId).defaultIfEmpty(new Portfolio(userId));
    }
}
