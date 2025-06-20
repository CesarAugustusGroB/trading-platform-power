package com.trading.platform.orderbook;

import java.util.Comparator;

import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;

import com.trading.platform.orderbook.TradeExecuted;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class OrderMatchingService {

    private final OrderRepository repository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public OrderMatchingService(OrderRepository repository, KafkaTemplate<String, Object> kafkaTemplate) {
        this.repository = repository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public Mono<Order> placeOrder(Order order) {
        return repository.save(order)
                .doOnSuccess(o -> kafkaTemplate.send("order.placed", o))
                .flatMap(this::match)
                .flatMap(saved -> {
                    if (saved.getStatus() != OrderStatus.NEW) {
                        TradeExecuted event = new TradeExecuted(
                                saved.getType() == OrderType.BUY ? saved.getId() : null,
                                saved.getType() == OrderType.SELL ? saved.getId() : null,
                                saved.getQuantity() - saved.getRemainingQuantity());
                        kafkaTemplate.send("order.matched", event);
                    }
                    return repository.save(saved);
                });
    }

    private Mono<Order> match(Order placed) {
        OrderType opposite = placed.getType() == OrderType.BUY ? OrderType.SELL : OrderType.BUY;
        return repository.findAll()
                .filter(o -> o.getSymbol().equals(placed.getSymbol()) &&
                              o.getType() == opposite &&
                              o.getRemainingQuantity() > 0)
                .sort(Comparator
                        .comparing(Order::getPrice, placed.getType() == OrderType.BUY
                                ? Comparator.naturalOrder()
                                : Comparator.reverseOrder())
                        .thenComparing(Order::getTimestamp))
                .next()
                .flatMap(match -> executeTrade(placed, match))
                .switchIfEmpty(Mono.just(placed));
    }

    private Mono<Order> executeTrade(Order o1, Order o2) {
        int qty = Math.min(o1.getRemainingQuantity(), o2.getRemainingQuantity());
        o1.setRemainingQuantity(o1.getRemainingQuantity() - qty);
        o2.setRemainingQuantity(o2.getRemainingQuantity() - qty);
        if (o1.getRemainingQuantity() == 0) {
            o1.setStatus(OrderStatus.FILLED);
        } else {
            o1.setStatus(OrderStatus.PARTIALLY_FILLED);
        }
        if (o2.getRemainingQuantity() == 0) {
            o2.setStatus(OrderStatus.FILLED);
        } else {
            o2.setStatus(OrderStatus.PARTIALLY_FILLED);
        }
        return repository.save(o2).thenReturn(o1);
    }

    public Flux<Order> getAll() {
        return repository.findAll();
    }

    public Mono<Order> getById(String id) {
        return repository.findById(id);
    }
}
