package com.trading.platform.wallet;

import java.math.BigDecimal;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/wallets")
public class WalletController {

    private final WalletService service;

    public WalletController(WalletService service) {
        this.service = service;
    }

    @PostMapping("/{userId}/deposit/{amount}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Wallet> deposit(@PathVariable String userId, @PathVariable BigDecimal amount) {
        return service.deposit(userId, amount);
    }

    @PostMapping("/{userId}/withdraw/{amount}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Wallet> withdraw(@PathVariable String userId, @PathVariable BigDecimal amount) {
        return service.withdraw(userId, amount);
    }
}
