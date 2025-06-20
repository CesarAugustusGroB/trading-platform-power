package com.trading.platform.trade;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/trades")
public class TradeController {

    private final TradeService service;

    public TradeController(TradeService service) {
        this.service = service;
    }

    @GetMapping
    public Flux<Trade> list() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Mono<Trade> get(@PathVariable String id) {
        return service.getById(id);
    }
}
