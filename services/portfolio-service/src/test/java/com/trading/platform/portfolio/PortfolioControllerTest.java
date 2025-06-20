package com.trading.platform.portfolio;

import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class PortfolioControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    PortfolioRepository repository;

    @Test
    void getPortfolioReturnsFromRepository() {
        Portfolio p = new Portfolio("u1");
        p.setPositions(Collections.singletonList(new Position("AAPL", 5)));
        when(repository.findById("u1")).thenReturn(Mono.just(p));

        webTestClient.get()
                .uri("/portfolios/u1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Portfolio.class).isEqualTo(p);
    }
}
