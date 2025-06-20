package com.trading.platform.trade;

import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Flux;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class TradeControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    TradeRepository repository;

    @Test
    void listTradesReturnsFromRepository() {
        Trade trade = new Trade("b1", "s1", 10);
        when(repository.findAll()).thenReturn(Flux.just(trade));

        webTestClient.get()
                .uri("/trades")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Trade.class).isEqualTo(Collections.singletonList(trade));
    }
}
