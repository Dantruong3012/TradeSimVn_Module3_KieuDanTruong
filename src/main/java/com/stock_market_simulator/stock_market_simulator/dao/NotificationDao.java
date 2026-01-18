package com.stock_market_simulator.stock_market_simulator.dao;

import com.stock_market_simulator.stock_market_simulator.model.entity.Notification;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NotificationDao {
    public void  insertIntoNotification(Connection connection, Notification notification) throws SQLException {
            String sql = "INSERT INTO notifications (user_id, message, type) VALUES (?,?,?)";
            try(PreparedStatement ps = connection.prepareStatement(sql)){
                    ps.setInt(1, notification.getUserId());
                    ps.setString(2, notification.getMessage());
                    ps.setString(3, notification.getType());
                    ps.executeUpdate();
            }
    }


    // lay danh sach thong bao limit 10 bang userId

    public List<Notification> getNotifiByUserId(Connection connection, int  id) throws  SQLException{
        String sql = "SELECT * FROM notifications WHERE user_id = ? ORDER BY created_at DESC LIMIT 10";
        List<Notification> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
           while (rs.next()){
               Notification notification = new Notification();
               notification.setId(rs.getInt("id"));
               notification.setUserId(rs.getInt("user_id"));
               notification.setMessage(rs.getString("message"));
               notification.setType(rs.getString("type"));
               notification.setSeen(rs.getBoolean("seen"));
               notification.setCreatedAt(rs.getTimestamp("created_at"));
               list.add(notification);
           }
        }
        return  list;
    }

    // CAP NHAT TRANG THAI THONG BAO
    public  void updateMessageStatus(Connection connection, int userId) throws SQLException{
        String sql = "UPDATE notifications SET seen = true WHERE user_id = ?";
        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }
}
