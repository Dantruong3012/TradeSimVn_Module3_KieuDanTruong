package com.stock_market_simulator.stock_market_simulator.services.impl;

import com.stock_market_simulator.stock_market_simulator.config.JdbcConnection;
import com.stock_market_simulator.stock_market_simulator.dao.*;
import com.stock_market_simulator.stock_market_simulator.model.entity.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

public class PlaceOrderService {
    private OrderDao orderDao = new OrderDao();
    private PortfolioDao portfolioDao = new PortfolioDao();
    private UserDao userDao = new UserDao();
    private NotificationDao notificationDao = new NotificationDao();
    private StockDao stockDao = new StockDao();

    public boolean processOrderBuy(Connection connection, Order order) throws SQLException {
        BigDecimal caculationPrice;
        if ("MP".equalsIgnoreCase(order.getOrderType())){
            Stock stock = stockDao.findStockBySymbol(connection,order.getSymbol());
            if (stock == null) return false;
            caculationPrice = stock.getCeilingPrice();
            order.setPrice(stock.getCeilingPrice());
        }else {
            caculationPrice = order.getPrice();
        }

        BigDecimal totalCost = caculationPrice.multiply(new BigDecimal(order.getQuantity())).multiply(new BigDecimal(1000));

        User currentUser = userDao.findUserByID(connection, order.getUserId());

        BigDecimal availableBalance = currentUser.getBalance().subtract(currentUser.getLockedBalance());

        if (availableBalance.compareTo(totalCost) < 0) {
            return false;
        }

        userDao.updateLockedBalance(connection, currentUser.getId(), totalCost);
        return true;
    }

    public boolean processOrderSell(Connection connection, Order order) throws SQLException {
        Portfolio stockInHand = portfolioDao.findByUserAndSymbol(connection, order.getUserId(), order.getSymbol());
        if (stockInHand == null) { return false; }
        int availableQty = stockInHand.getQuantity() - stockInHand.getBlockedQty();
        if (availableQty < order.getQuantity()) { return false; }
        if ("MP".equalsIgnoreCase(order.getOrderType())) {
            Stock stock = stockDao.findStockBySymbol(connection, order.getSymbol());
            if (stock == null) return false;
            order.setPrice(stock.getFloorPrice());
        }
        portfolioDao.updateStockLockedQty(connection, order.getQuantity(), order.getSymbol(), order.getUserId());
        return true;
    }

    public boolean orderProcessing(Order order) {
        Connection connection = null;
        try {
            connection = JdbcConnection.getConnection();
            connection.setAutoCommit(false);

            if ("BUY".equalsIgnoreCase(order.getSide())) {
                boolean isBuy = processOrderBuy(connection, order);
                if (!isBuy) {
                    connection.rollback();
                    return false;
                }
            } else if ("SELL".equalsIgnoreCase(order.getSide())) {
                boolean isSell = processOrderSell(connection, order);
                if (!isSell) {
                    connection.rollback();
                    return false;
                }
            }
            orderDao.insertIntoOrder(connection, order);

            Notification notification = new Notification();
            notification.setUserId(order.getUserId());
            String msg = String.format("Đặt lệnh thành công: %s %s mã %s, giá %s, KL %d",
                    "BUY".equals(order.getSide()) ? "MUA" : "BÁN",
                    order.getOrderType(),
                    order.getSymbol(),
                    order.getPrice().toString(),
                    order.getQuantity());
            notification.setMessage(msg);
            notification.setType("SUCCESS");

            notificationDao.insertIntoNotification(connection, notification);
            connection.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (connection != null) { connection.rollback(); }
            } catch (SQLException ex) { ex.printStackTrace(); }
            return false;
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                    connection.close();
                }
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}