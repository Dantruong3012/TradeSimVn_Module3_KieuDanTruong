package com.stock_market_simulator.stock_market_simulator.services.impl;

import com.stock_market_simulator.stock_market_simulator.config.JdbcConnection;
import com.stock_market_simulator.stock_market_simulator.dao.OrderDao;
import com.stock_market_simulator.stock_market_simulator.dao.StockDao;
import com.stock_market_simulator.stock_market_simulator.dto.OrderDto;
import com.stock_market_simulator.stock_market_simulator.model.entity.Stock;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StockService {
    private StockDao stockDao = new StockDao();
    private OrderDao orderDao = new OrderDao();

    public List<Stock> getStockMarket() {
        Connection connection = null;
        try {
            connection = JdbcConnection.getConnection();
            List<Stock> stockList = stockDao.findAllStocks(connection);

            for (Stock s : stockList) {
                // TÍNH TOÁN CÁC CHỈ SỐ CƠ BẢN
                BigDecimal ref = s.getRefPrice();
                BigDecimal current = s.getCurrentPrice();

                if (ref != null && ref.compareTo(BigDecimal.ZERO) > 0) {
                    // Tính Trần / Sàn
                    s.setCeilingPrice(ref.multiply(new BigDecimal("1.07")).setScale(2, RoundingMode.FLOOR));
                    s.setFloorPrice(ref.multiply(new BigDecimal("0.93")).setScale(2, RoundingMode.CEILING));

                    // Tính % Thay đổi (Chỉ khi có giá khớp)
                    if (current != null && current.compareTo(BigDecimal.ZERO) > 0) {
                        BigDecimal changePrice = current.subtract(ref);
                        s.setChangePrice(changePrice);

                        BigDecimal percent = changePrice.divide(ref, 4, RoundingMode.HALF_UP)
                                .multiply(new BigDecimal("100"))
                                .setScale(2, RoundingMode.HALF_UP);
                        s.setChangePercent(percent);
                    }
                }

                // lay top 3 lenh mua ban gan voi gia tran san nhat
                // dong thoi su dung ham de lap day khoang trong de tranh loi indexOutOf.... vi co nhung ma chi dat
                // 2 lan co the thieu
                List<OrderDto> bestBuy = orderDao.getTopThreeGoodPrice(connection, s.getSymbol(), "BUY");
                s.setTopBuy(fillEmptySlots(bestBuy));

                List<OrderDto> bestSell = orderDao.getTopThreeGoodPrice(connection, s.getSymbol(), "SELL");
                s.setTopSell(fillEmptySlots(bestSell));
            }
            return stockList;

        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            try { if (connection != null) connection.close(); } catch (Exception ex) {}
        }
    }

   // ham phu tao ra mot dummy opting trong truong hop khong co du 3 opting tranh loi index ben jsp
    private List<OrderDto> fillEmptySlots(List<OrderDto> list) {
        if (list == null) list = new ArrayList<>();
        while (list.size() < 3) {
            list.add(new OrderDto());
        }
        return list;
    }
}