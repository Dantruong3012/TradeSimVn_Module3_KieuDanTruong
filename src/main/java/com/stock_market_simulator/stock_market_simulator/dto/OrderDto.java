package com.stock_market_simulator.stock_market_simulator.dto;

import java.math.BigDecimal;

public class OrderDto {
    private String symbol;
    private  String side;
    private  BigDecimal price;
    private Integer totalvolume;

    public OrderDto() {
    }

    public OrderDto(String symbol, String side, BigDecimal price, Integer totalvolume) {
        this.symbol = symbol;
        this.side = side;
        this.price = price;
        this.totalvolume =  totalvolume;
    }

    public OrderDto(String symbol, String side, Integer totalvolume, BigDecimal price) {
        this.symbol = symbol;
        this.side = side;
        this.totalvolume = totalvolume;
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public Integer getTotalVolume() {
        return totalvolume;
    }

    public void setTotalVolume(Integer totalvolume) {
        this.totalvolume = totalvolume;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
