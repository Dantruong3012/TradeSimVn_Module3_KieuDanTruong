package com.stock_market_simulator.stock_market_simulator.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionDto {
    private String symbol;
    private BigDecimal price;
    private Integer volume;
    private String side;
    private LocalDateTime createdTime;

    public TransactionDto() {
    }

    public TransactionDto(String symbol, BigDecimal price, Integer volume, String side) {
        this.symbol = symbol;
        this.price = price;
        this.volume = volume;
        this.side = side;
    }

    public TransactionDto(String symbol, BigDecimal price, Integer volume, LocalDateTime createdTime) {
        this.symbol = symbol;
        this.price = price;
        this.volume = volume;
        this.createdTime = createdTime;
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

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

}
