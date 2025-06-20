package com.trading.platform.pricefeed;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;

class PriceFeedLambdaTest {

    @Test
    void handleRequestPublishesAndStores() {
        @SuppressWarnings("unchecked")
        KafkaProducer<String, String> producer = Mockito.mock(KafkaProducer.class);
        @SuppressWarnings("unchecked")
        MongoCollection<Document> collection = Mockito.mock(MongoCollection.class);
        PriceFeedLambda lambda = new PriceFeedLambda(producer, collection, "topic", "AAPL");

        lambda.handleRequest(null, null);

        verify(producer).send(Mockito.any(ProducerRecord.class));
        verify(collection).updateOne(any(), any(), Mockito.any(UpdateOptions.class));
    }
}
