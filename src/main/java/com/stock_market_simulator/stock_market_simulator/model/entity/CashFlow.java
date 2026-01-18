package com.stock_market_simulator.stock_market_simulator.model.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CashFlow {
    private Integer id;
    private Integer userId;
    private BigDecimal amount;       // Số tiền biến động (có thể âm hoặc dương)
    private BigDecimal balanceAfter; // Số dư sau biến động
    private String flowType;         // DEPOSIT, WITHDRAW, BUY_STOCK...
    private String description;
    private LocalDateTime createdTime;

    public CashFlow() {
    }

    public CashFlow(Integer id, Integer userId, BigDecimal amount, BigDecimal balanceAfter, String flowType, String description, LocalDateTime createdTime) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.flowType = flowType;
        this.description = description;
        this.createdTime = createdTime;
    }

    public CashFlow(Integer userId, BigDecimal amount, BigDecimal balanceAfter, String flowType, String description) {
        this.userId = userId;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.flowType = flowType;
        this.description = description;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getBalanceAfter() {
        return balanceAfter;
    }

    public void setBalanceAfter(BigDecimal balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

    public String getFlowType() {
        return flowType;
    }

    public void setFlowType(String flowType) {
        this.flowType = flowType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }
}
