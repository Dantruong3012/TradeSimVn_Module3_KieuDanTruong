package com.stock_market_simulator.stock_market_simulator.services.impl;

import com.stock_market_simulator.stock_market_simulator.config.JdbcConnection;
import com.stock_market_simulator.stock_market_simulator.dao.PortfolioDao;
import com.stock_market_simulator.stock_market_simulator.dao.StockDao;
import com.stock_market_simulator.stock_market_simulator.dto.PortfolioDto;
import com.stock_market_simulator.stock_market_simulator.model.entity.Portfolio;
import com.stock_market_simulator.stock_market_simulator.model.entity.Stock;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PortfolioService {
    private StockDao stockDao = new StockDao();
    private PortfolioDao portfolioDao = new PortfolioDao();

    public List<PortfolioDto> getAssetIndexDetail(int userId){
        List<PortfolioDto> list = new ArrayList<>();
        Connection connection = null;
        try {
            connection = JdbcConnection.getConnection();
            List<Portfolio> rawList = portfolioDao.findByUserId(connection, userId);
            for(Portfolio element : rawList){

                Stock stock = stockDao.findStockBySymbol(connection ,element.getSymbol());
                // su ly tranh loi null pointer trong truong hop bat dong bo giua 2 bang porfolio va stock
                if (stock == null){
                    continue;
                }
                BigDecimal stockCurrentPrice = stock.getCurrentPrice();
                String company = stock.getCompanyName();
                BigDecimal marketValue = stockCurrentPrice.multiply(new BigDecimal(element.getQuantity()));
                BigDecimal costValue = element.getAvgPrice().multiply(new BigDecimal(element.getQuantity()));
                BigDecimal gainLoss =  marketValue.subtract(costValue);
                Double gainlossPercent = 0.0;
                if (costValue.compareTo(BigDecimal.ZERO) > 0){
                    gainlossPercent = gainLoss.divide(costValue, 4, RoundingMode.HALF_UP).doubleValue() * 100; // tinh %
                }

                PortfolioDto portfolioDto = new PortfolioDto();
                portfolioDto.setSymbol(element.getSymbol());
                portfolioDto.setCompanyName(company);
                portfolioDto.setQuantity(element.getQuantity());
                portfolioDto.setAvgPrice(element.getAvgPrice());
                portfolioDto.setCurrentPrice(stockCurrentPrice);
                portfolioDto.setMarketValue(marketValue);
                portfolioDto.setGainLoss(gainLoss);
                portfolioDto.setGainlossPercent(gainlossPercent);
                list.add(portfolioDto);
            }
            return  list;
        }catch (SQLException e){
            e.printStackTrace();
            return  new ArrayList<>();
        }finally {
            try{
                if (connection != null){connection.close();}
            }catch (SQLException ex){ex.printStackTrace();}
        }
    }
}

// TUONG LAI CAN TOI UU HOA QUERY HIEN TAI QUERY DANG CHAM
// VI MOI LAN CHI LAY RA DUOC 1 MA CP CAN SUA TRONG THOI GIAN TOI
