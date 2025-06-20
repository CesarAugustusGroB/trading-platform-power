package com.trading.platform.orderbook;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class OrderControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    OrderRepository repository;

    @MockBean
    KafkaTemplate<String, Object> kafkaTemplate;

    @Test
    void placeOrderPublishesEvent() {
        Order order = new Order("AAPL", BigDecimal.valueOf(10), 5, OrderType.BUY);
        when(repository.save(order)).thenReturn(Mono.just(order));

        webTestClient.post()
                .uri("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(order)
                .exchange()
                .expectStatus().isCreated();

        verify(kafkaTemplate).send("order.placed", order);
    }
}
