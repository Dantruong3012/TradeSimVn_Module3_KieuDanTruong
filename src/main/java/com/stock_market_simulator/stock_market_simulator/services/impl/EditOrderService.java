package com.stock_market_simulator.stock_market_simulator.services.impl;

import com.stock_market_simulator.stock_market_simulator.config.JdbcConnection;
import com.stock_market_simulator.stock_market_simulator.dao.*;
import com.stock_market_simulator.stock_market_simulator.model.entity.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

public class EditOrderService {
    private UserDao userDao = new UserDao();
    private OrderDao orderDao = new OrderDao();
    private PortfolioDao portfolioDao = new PortfolioDao();
    private NotificationDao notificationDao = new NotificationDao();
    private StockDao stockDao = new StockDao();

    public String processOrderEdit(int orderId, int userId, String symbol, int newQty, BigDecimal newPrice) {
        Connection connection = null;
        try {
            connection = JdbcConnection.getConnection();
            connection.setAutoCommit(false);

            Order oldOrder = orderDao.findOrderById(connection, orderId);

            if (oldOrder == null) {
                return "Lệnh này không tồn tại";
            } else if (oldOrder.getUserId() != userId) {
                return "Bạn đang cố huỷ lệnh không phải của mình";
            } else if (!"PENDING".equalsIgnoreCase(oldOrder.getStatus())) {
                return "Chỉ được sửa lệnh đang Chờ khớp!";
            }

            if (!"MP".equalsIgnoreCase(oldOrder.getOrderType())) {
                Stock stock = stockDao.findStockBySymbol(connection, symbol);
                if (stock != null) {
                    if (newPrice.compareTo(stock.getFloorPrice()) < 0) {
                        return "Giá đặt không được thấp hơn giá sàn (" + stock.getFloorPrice() + ")";
                    }
                    if (newPrice.compareTo(stock.getCeilingPrice()) > 0) {
                        return "Giá đặt không được cao hơn giá trần (" + stock.getCeilingPrice() + ")";
                    }
                }
            }

            if ("BUY".equalsIgnoreCase(oldOrder.getSide())) {
                BigDecimal oldTotalCost = oldOrder.getPrice().multiply(new BigDecimal(oldOrder.getQuantity())).multiply(new BigDecimal(1000));
                BigDecimal newTotalCost = newPrice.multiply(new BigDecimal(newQty)).multiply(new BigDecimal(1000));
                BigDecimal priceSpread = newTotalCost.subtract(oldTotalCost);

                if (priceSpread.compareTo(BigDecimal.ZERO) > 0) {
                    User user = userDao.findUserByID(connection, userId);
                    BigDecimal avaliableBalance = user.getBalance().subtract(user.getLockedBalance());

                    if (avaliableBalance.compareTo(priceSpread) < 0) {
                        connection.rollback();
                        return "Số tiền khả dụng của bạn hiện tại không đủ để mua thêm";
                    }
                    userDao.updateLockedBalance(connection, userId, priceSpread);

                } else if (priceSpread.compareTo(BigDecimal.ZERO) < 0) {
                    userDao.updateUserBalance(connection, userId, BigDecimal.ZERO, priceSpread);
                }

            } else if ("SELL".equalsIgnoreCase(oldOrder.getSide())) {
                Portfolio portfolio = portfolioDao.findByUserAndSymbol(connection, userId, symbol);
                if (portfolio == null) {
                    connection.rollback();
                    return "Lỗi: Không tìm thấy danh mục đầu tư!";
                }

                int oldQty = oldOrder.getQuantity();
                int qtySpread = newQty - oldQty;

                if (qtySpread > 0) {
                    int availableQty = portfolio.getQuantity() - portfolio.getBlockedQty();
                    if (availableQty < qtySpread) {
                        connection.rollback();
                        return "Không đủ cổ phiếu trong kho để bán thêm!";
                    }
                    portfolioDao.updateStockLockedQty(connection, qtySpread, symbol, userId);

                } else if (qtySpread < 0) {
                    portfolioDao.updateStockLockedQty(connection, qtySpread, symbol, userId);
                }
            }

            boolean isSuccess = orderDao.updateOrder(connection, orderId, userId, symbol, newPrice, newQty);
            if (!isSuccess) {
                connection.rollback();
                return "Lỗi cập nhật lệnh vào Database!";
            }

            Notification notifi = new Notification();
            notifi.setUserId(userId);
            String message = String.format("Đã sửa lệnh %s mã %s thành công. Giá: %s, KL: %d",
                    oldOrder.getSide(), symbol, newPrice, newQty);
            notifi.setMessage(message);
            notifi.setType("SUCCESS");
            notifi.setSeen(false);
            notificationDao.insertIntoNotification(connection, notifi);

            connection.commit();
            return "SUCCESS";

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (connection != null) { connection.rollback(); }
            } catch (SQLException ex) { ex.printStackTrace(); }
            return "Lỗi hệ thống!" + e.getMessage();
        } finally {
            try {
                if (connection != null) { connection.setAutoCommit(true); connection.close(); }
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}