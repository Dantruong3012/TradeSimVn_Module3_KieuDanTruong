package com.stock_market_simulator.stock_market_simulator.services.impl;

import com.stock_market_simulator.stock_market_simulator.config.JdbcConnection;
import com.stock_market_simulator.stock_market_simulator.controller.MarketController.OrderController;
import com.stock_market_simulator.stock_market_simulator.dao.NotificationDao;
import com.stock_market_simulator.stock_market_simulator.dao.OrderDao;
import com.stock_market_simulator.stock_market_simulator.dao.PortfolioDao;
import com.stock_market_simulator.stock_market_simulator.dao.UserDao;
import com.stock_market_simulator.stock_market_simulator.model.entity.Notification;
import com.stock_market_simulator.stock_market_simulator.model.entity.Order;


import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

public class CancelOrderService {
    private UserDao userDao = new UserDao();
    private  PortfolioDao portfolioDao = new PortfolioDao();
    private OrderDao orderDao = new OrderDao();
    private NotificationDao notifiDao = new NotificationDao();

    public  String cancelOrder(String symbol, Order order, int userId){
        Connection connection = null;
        try{
            connection = JdbcConnection.getConnection();
            connection.setAutoCommit(false);
            if (order.getUserId() != userId){
                return "Bạn không có quyền huỷ lệnh này";
            }

            if (!"PENDING".equalsIgnoreCase(order.getStatus()) && !"PARTIAL".equalsIgnoreCase(order.getStatus())){
                return "Lệnh này đã được khơp hết";
            }

            int remaingQty = order.getQuantity() - order.getMatchedQty();

            if (remaingQty <= 0){
                return "Lệnh này đã được khớp hoàn toàn";
            }

            if ("BUY".equalsIgnoreCase(order.getSide())){
                BigDecimal lockPrice = order.getPrice();
                BigDecimal refundPrice = lockPrice.multiply(new BigDecimal(remaingQty)).multiply(new BigDecimal(1000));
                userDao.updateLockedBalance(connection, userId, refundPrice.negate());
            }else if ("SELL".equalsIgnoreCase(order.getSide())){
                portfolioDao.updateStockLockedQty(connection, -remaingQty, symbol, userId);
            }
            orderDao.updateAfterOrder(connection, order.getId(), order.getMatchedQty(), "CANCELLED");
            Notification notifi = new Notification();
            notifi.setUserId(userId);
            notifi.setMessage("Đã hủy lệnh " + order.getSide() + " mã " + symbol + ". Hoàn trả: " +
                    ("BUY".equals(order.getSide()) ? "Tiền" : "Cổ phiếu"));
            notifi.setType("SUCCESS");
            notifi.setSeen(false);
            notifiDao.insertIntoNotification(connection, notifi);
            connection.commit();
            return "SUCCESS";
        }catch (SQLException e){
            e.printStackTrace();
            try {
                if (connection != null){
                    connection.rollback();
                }
            }catch (SQLException ex){
                ex.printStackTrace();
            }
            return "Hệ thống tạm thời lỗi";
        }finally {
            try {
                if ( connection != null){
                    connection.close();
                }
            }catch (SQLException e){e.printStackTrace();}
        }
    }
}