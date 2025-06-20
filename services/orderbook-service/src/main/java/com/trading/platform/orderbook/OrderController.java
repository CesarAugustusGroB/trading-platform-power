package com.trading.platform.orderbook;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderMatchingService service;

    public OrderController(OrderMatchingService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Order> place(@RequestBody Order order) {
        return service.placeOrder(order);
    }

    @GetMapping
    public Flux<Order> list() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Mono<Order> get(@PathVariable String id) {
        return service.getById(id);
    }
}
