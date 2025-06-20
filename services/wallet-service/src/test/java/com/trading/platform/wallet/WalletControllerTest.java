package com.trading.platform.wallet;

import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class WalletControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    WalletRepository repository;

    @Test
    void depositUpdatesBalance() {
        Wallet wallet = new Wallet("u1");
        when(repository.findById("u1")).thenReturn(Mono.just(wallet));
        when(repository.save(wallet)).thenReturn(Mono.just(wallet));

        webTestClient.post()
                .uri("/wallets/u1/deposit/10")
                .exchange()
                .expectStatus().isOk();
    }
}
