package com.trading.platform.trade;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TradeService {

    private final TradeRepository repository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public TradeService(TradeRepository repository, KafkaTemplate<String, Object> kafkaTemplate) {
        this.repository = repository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "order.matched", groupId = "trade-service")
    public void handleOrderMatched(TradeExecuted event) {
        Trade trade = new Trade(event.getBuyOrderId(), event.getSellOrderId(), event.getQuantity());
        repository.save(trade)
                .doOnSuccess(t -> kafkaTemplate.send("trade.executed", t))
                .subscribe();
    }

    @KafkaListener(topics = "trade.executed", groupId = "trade-service-sink")
    public void handleTradeExecuted(Trade trade) {
        repository.save(trade).subscribe();
    }

    public Flux<Trade> getAll() {
        return repository.findAll();
    }

    public Mono<Trade> getById(String id) {
        return repository.findById(id);
    }
}
