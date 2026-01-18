package com.stock_market_simulator.stock_market_simulator.model.entity;

import java.math.BigDecimal;

public class Portfolio {
    private Integer userId;
    private String symbol;
    private Integer quantity;
    private BigDecimal avgPrice;

    public Portfolio() {}

    public Portfolio(Integer userId, String symbol, BigDecimal avgPrice) {
        this.userId = userId;
        this.symbol = symbol;
        this.quantity = 0;
        this.avgPrice = avgPrice;
    }

    public Portfolio(Integer userId, String symbol, Integer quantity, BigDecimal avgPrice) {
        this.userId = userId;
        this.symbol = symbol;
        this.quantity = quantity;
        this.avgPrice = avgPrice;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(BigDecimal avgPrice) {
        this.avgPrice = avgPrice;
    }
}
