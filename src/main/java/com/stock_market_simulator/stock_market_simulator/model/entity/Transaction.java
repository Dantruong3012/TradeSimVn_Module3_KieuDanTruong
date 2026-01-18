package com.stock_market_simulator.stock_market_simulator.model.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {
    private Integer id;
    private Integer buyOrderId;
    private Integer sellOrderId;
    private String symbol;
    private BigDecimal price;  // Giá khớp thực tế
    private Integer volume;        // Khối lượng khớp
    private LocalDateTime createdTime;

    public Transaction() {}

    public Transaction(Integer buyOrderId, Integer sellOrderId, String symbol, BigDecimal price, Integer volume, LocalDateTime createdTime) {
        this.buyOrderId = buyOrderId;
        this.sellOrderId = sellOrderId;
        this.symbol = symbol;
        this.price = price;
        this.volume = volume;
        this.createdTime = createdTime;
    }



    public Transaction(Integer id, Integer buyOrderId, Integer sellOrderId, String symbol, BigDecimal price, Integer volume, LocalDateTime createdTime) {
        this.id = id;
        this.buyOrderId = buyOrderId;
        this.sellOrderId = sellOrderId;
        this.symbol = symbol;
        this.price = price;
        this.volume = volume;
        this.createdTime = createdTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBuyOrderId() {
        return buyOrderId;
    }

    public void setBuyOrderId(Integer buyOrderId) {
        this.buyOrderId = buyOrderId;
    }

    public Integer getSellOrderId() {
        return sellOrderId;
    }

    public void setSellOrderId(Integer sellOrderId) {
        this.sellOrderId = sellOrderId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }
}
