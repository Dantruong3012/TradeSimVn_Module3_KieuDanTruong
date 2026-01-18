package com.stock_market_simulator.stock_market_simulator.dto;

import java.math.BigDecimal;

public class PortfolioDto {
    private  String symbol;
    private  String companyName;
    private  Integer quantity;
    private BigDecimal avgPrice;
    private BigDecimal currentPrice;
    private  BigDecimal marketValue; // tong gia tri hien tai cua co phieu = qty * currentP
    private  BigDecimal gainLoss; // lai lo theo tien
    private  Double gainlossPercent; // lai lo %


    public PortfolioDto() {
    }


    public PortfolioDto(String symbol, String companyName, Integer quantity, BigDecimal avgPrice, BigDecimal currentPrice, BigDecimal marketValue, BigDecimal gainLoss, Double gainlossPercent) {
        this.symbol = symbol;
        this.companyName = companyName;
        this.quantity = quantity;
        this.avgPrice = avgPrice;
        this.currentPrice = currentPrice;
        this.marketValue = marketValue;
        this.gainLoss = gainLoss;
        this.gainlossPercent = gainlossPercent;
    }


    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    public BigDecimal getMarketValue() {
        return marketValue;
    }

    public void setMarketValue(BigDecimal marketValue) {
        this.marketValue = marketValue;
    }

    public BigDecimal getGainLoss() {
        return gainLoss;
    }

    public void setGainLoss(BigDecimal gainLoss) {
        this.gainLoss = gainLoss;
    }

    public Double getGainlossPercent() {
        return gainlossPercent;
    }

    public void setGainlossPercent(Double gainlossPercent) {
        this.gainlossPercent = gainlossPercent;
    }
}
