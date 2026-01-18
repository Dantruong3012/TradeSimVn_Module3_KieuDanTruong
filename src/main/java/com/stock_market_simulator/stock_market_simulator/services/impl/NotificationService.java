package com.stock_market_simulator.stock_market_simulator.services.impl;

import com.stock_market_simulator.stock_market_simulator.config.JdbcConnection;
import com.stock_market_simulator.stock_market_simulator.dao.NotificationDao;
import com.stock_market_simulator.stock_market_simulator.model.entity.Notification;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NotificationService {
    private NotificationDao notificationDao = new NotificationDao();

    public List<Notification> getNotifiByUser(int userId){
        Connection connection = null;
        try {
            connection = JdbcConnection.getConnection();
            return notificationDao.getNotifiByUserId(connection, userId);
        }catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }finally {
            try {
                if (connection != null){connection.close();}
            }catch (SQLException ex){ex.printStackTrace();}
        }
    }


    public  void markAsRead(int userId){
        Connection connection = null;
        try {
            connection = JdbcConnection.getConnection();
            notificationDao.updateMessageStatus(connection, userId);
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                if (connection != null){
                    connection.close();
                }
            }catch (SQLException ex){ex.printStackTrace();}
        }
    }
}
