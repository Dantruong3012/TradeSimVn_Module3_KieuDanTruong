package com.stock_market_simulator.stock_market_simulator.dto;

import java.math.BigDecimal;

public class StockHistoryDto {
    private  String timeStr;
    private BigDecimal price;

    public StockHistoryDto() {
    }

    public StockHistoryDto(String timeStr, BigDecimal price) {
        this.timeStr = timeStr;
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getTimeStr() {
        return timeStr;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }
}
