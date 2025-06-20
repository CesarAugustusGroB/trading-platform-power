package com.trading.platform.pricefeed;

import java.time.Instant;
import java.util.Properties;
import java.util.Random;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.bson.Document;

public class PriceFeedLambda implements RequestHandler<Object, String> {

    private final KafkaProducer<String, String> producer;
    private final MongoCollection<Document> collection;
    private final ObjectMapper mapper = new ObjectMapper();
    private final Random random = new Random();
    private final String topic;
    private final String symbol;

    public PriceFeedLambda() {
        this(initProducer(), initCollection(),
                System.getenv().getOrDefault("KAFKA_TOPIC", "market.price"),
                System.getenv().getOrDefault("SYMBOL", "AAPL"));
    }

    public PriceFeedLambda(KafkaProducer<String, String> producer,
                           MongoCollection<Document> collection,
                           String topic,
                           String symbol) {
        this.producer = producer;
        this.collection = collection;
        this.topic = topic;
        this.symbol = symbol;
    }

    private static KafkaProducer<String, String> initProducer() {
        String kafka = System.getenv().getOrDefault("KAFKA_BOOTSTRAP_SERVERS", "localhost:9092");
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return new KafkaProducer<>(props);
    }

    private static MongoCollection<Document> initCollection() {
        String mongoUri = System.getenv().getOrDefault("MONGODB_URI", "mongodb://localhost:27017");
        MongoClient client = MongoClients.create(mongoUri);
        MongoDatabase db = client.getDatabase("trading");
        return db.getCollection("market_ticks");
    }

    @Override
    public String handleRequest(Object input, Context context) {
        double price = 100 + random.nextDouble() * 10;
        PriceTick tick = new PriceTick(symbol, price, Instant.now());
        try {
            String json = mapper.writeValueAsString(tick);
            producer.send(new ProducerRecord<>(topic, symbol, json));
            Document doc = new Document()
                    .append("_id", symbol)
                    .append("price", price)
                    .append("timestamp", tick.getTimestamp());
            collection.updateOne(Filters.eq("_id", symbol), new Document("$set", doc),
                    new UpdateOptions().upsert(true));
            return json;
        } catch (Exception e) {
            if (context != null) {
                context.getLogger().log("Error processing tick: " + e.getMessage());
            }
            throw new RuntimeException(e);
        }
    }
}
