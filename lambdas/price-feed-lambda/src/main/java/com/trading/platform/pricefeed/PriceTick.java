package com.trading.platform.pricefeed;

import java.time.Instant;

public class PriceTick {
    private String symbol;
    private double price;
    private Instant timestamp;

    public PriceTick() {
    }

    public PriceTick(String symbol, double price, Instant timestamp) {
        this.symbol = symbol;
        this.price = price;
        this.timestamp = timestamp;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
