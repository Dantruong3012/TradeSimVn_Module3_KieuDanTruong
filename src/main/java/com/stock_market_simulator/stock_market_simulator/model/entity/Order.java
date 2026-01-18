package com.stock_market_simulator.stock_market_simulator.model.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Order {
    private Integer id;
    private  Integer userId;
    private String symbol;
    private String orderType;
    private String side;
    private String status;
    private BigDecimal price;
    private Integer quantity;
    private Integer matchedQty;
    private LocalDateTime createdTime;

    public Order() {
    }

    public Order(Integer userId, String symbol, String orderType, String side, String status, BigDecimal price, Integer quantity) {
        this.userId = userId;
        this.symbol = symbol;
        this.orderType = orderType;
        this.side = side;
        this.status = status;
        this.price = price;
        this.quantity = quantity;
    }

    public Order(Integer id, Integer userId, String symbol, String orderType, String side, String status, BigDecimal price, Integer quantity, Integer matchedQty, LocalDateTime createdTime) {
        this.id = id;
        this.userId = userId;
        this.symbol = symbol;
        this.orderType = orderType;
        this.side = side;
        this.status = status;
        this.price = price;
        this.quantity = quantity;
        this.matchedQty = matchedQty;
        this.createdTime = createdTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getMatchedQty() {
        return matchedQty;
    }

    public void setMatchedQty(Integer matchedQty) {
        this.matchedQty = matchedQty;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }
}
