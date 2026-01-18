package com.stock_market_simulator.stock_market_simulator.model.entity;

import com.stock_market_simulator.stock_market_simulator.dto.OrderDto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Stock {
    private String symbol;
    private String companyName;
    private String exchange;
    private BigDecimal currentPrice;
    private BigDecimal changePrice;
    private BigDecimal changePercent;
    private BigDecimal refPrice;
    private BigDecimal ceilingPrice;
    private BigDecimal floorPrice;
    private Integer volume;
    private List<OrderDto> topBuy = new ArrayList<>();
    private List<OrderDto> topSell = new ArrayList<>();

    public Stock() {
    }

    public Stock(String symbol, String companyName, BigDecimal currentPrice, BigDecimal changePrice, BigDecimal changePercent, BigDecimal refPrice, BigDecimal ceilingPrice, BigDecimal floorPrice, String exchange) {
        this.symbol = symbol;
        this.companyName = companyName;
        this.currentPrice = currentPrice;
        this.changePrice = changePrice;
        this.changePercent = changePercent;
        this.refPrice = refPrice;
        this.ceilingPrice = ceilingPrice;
        this.floorPrice = floorPrice;
        this.exchange = exchange;
    }


    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getExchange() { return exchange; }
    public void setExchange(String exchange) { this.exchange = exchange; }

    public BigDecimal getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(BigDecimal currentPrice) { this.currentPrice = currentPrice; }

    public BigDecimal getChangePrice() { return changePrice; }
    public void setChangePrice(BigDecimal changePrice) { this.changePrice = changePrice; }

    public BigDecimal getChangePercent() { return changePercent; }
    public void setChangePercent(BigDecimal changePercent) { this.changePercent = changePercent; }

    public BigDecimal getRefPrice() { return refPrice; }
    public void setRefPrice(BigDecimal refPrice) { this.refPrice = refPrice; }

    public BigDecimal getCeilingPrice() { return ceilingPrice; }
    public void setCeilingPrice(BigDecimal ceilingPrice) { this.ceilingPrice = ceilingPrice; }

    public BigDecimal getFloorPrice() { return floorPrice; }
    public void setFloorPrice(BigDecimal floorPrice) { this.floorPrice = floorPrice; }

    public Integer getVolume() { return volume; }
    public void setVolume(Integer volume) { this.volume = volume; }

    public List<OrderDto> getTopBuy() {
        return topBuy;
    }
    public void setTopBuy(List<OrderDto> topBuy) {
        this.topBuy = topBuy;
    }

    public List<OrderDto> getTopSell() {
        return topSell;
    }
    public void setTopSell(List<OrderDto> topSell) {
        this.topSell = topSell;
    }
}