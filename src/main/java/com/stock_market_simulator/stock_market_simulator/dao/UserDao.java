package com.stock_market_simulator.stock_market_simulator.dao;

import com.stock_market_simulator.stock_market_simulator.config.JdbcConnection;
import com.stock_market_simulator.stock_market_simulator.model.entity.User;

import java.math.BigDecimal;
import java.sql.*;

public class UserDao {

    // ĐĂNG KÝ TÀI KHOẢN (OK)
    public int registerNewUser(Connection connection, User user) throws SQLException {
        String sql = "INSERT INTO users (user_name, display_name, user_password, confirm_password, security_code) VALUES (?,?,?,?,?)";

        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1, user.getUserName());
            ps.setString(2, user.getDisplayName());
            ps.setString(3, user.getUserPassword());
            ps.setString(4, user.getConfirmPassword());
            ps.setString(5, user.getSecurityCode());

            if (ps.executeUpdate() > 0){
                try(ResultSet rs = ps.getGeneratedKeys()){
                    if (rs.next()){
                        return rs.getInt(1);
                    }
                }
            }
        }
        return 0;
    }

    // CHECK USERNAME
    public boolean isUserNameExist(String userName){
        String sql = "SELECT COUNT(1) FROM users WHERE user_name = ?";
        try(Connection connection = JdbcConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setString(1, userName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    // TÌM USER THEO ID
    public User findUserByID(Connection connection, int userId) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";
        try(PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUserName(rs.getString("user_name"));
                user.setDisplayName(rs.getString("display_name"));
                user.setUserPassword(rs.getString("user_password"));
                user.setBalance(rs.getBigDecimal("balance"));
                user.setLockedBalance(rs.getBigDecimal("locked_balance"));
                user.setSecurityCode(rs.getString("security_code"));
                return user;
            }
        }
        return null;
    }

    //  TÌM USER THEO USERNAME
    public User findUserByUserName(Connection connection, String userName) throws SQLException {
        String sql = "SELECT * FROM users WHERE user_name = ?";

        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, userName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUserName(rs.getString("user_name"));
                user.setDisplayName(rs.getString("display_name"));
                user.setUserPassword(rs.getString("user_password"));
                user.setBalance(rs.getBigDecimal("balance"));
                user.setLockedBalance(rs.getBigDecimal("locked_balance"));
                user.setSecurityCode(rs.getString("security_code"));
                return user;
            }
        }
        return null;
    }

    // CẬP NHẬT SỐ DƯ (OK) tai khoan chinh va tai khoan treo
    public void updateUserBalance(Connection connection, int userId, BigDecimal newBalance, BigDecimal newLockedBalance) throws SQLException {
        String sql = "UPDATE users SET balance = balance + ?, locked_balance = locked_balance + ? WHERE id = ?";
        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setBigDecimal(1, newBalance);
            ps.setBigDecimal(2, newLockedBalance);
            ps.setInt(3, userId);
            ps.executeUpdate();
        }
    }

    // dung cho viec dat lenh huy lenh va thay doi lenh
public  void  updateLockedBalance   (Connection connection, int userId, BigDecimal newLockedBalance) throws  SQLException{
    String sql = "UPDATE users SET locked_balance = locked_balance + ? WHERE id = ?";
    try(PreparedStatement ps = connection.prepareStatement(sql)){
        ps.setBigDecimal(1, newLockedBalance);
        ps.setInt(2, userId);
        ps.executeUpdate();
    }
}


    // UPDATE MK

    public boolean updateUserPassword(Connection connection, String newHashPassword, int userId) throws  SQLException{
        String sql = "UPDATE users SET user_password = ? WHERE id = ?";
        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, newHashPassword);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean updateUserConfirmPassword(Connection connection, String newHashConfirmPassword, int userId) throws  SQLException{
        String sql = "UPDATE users SET confirm_password = ? WHERE id = ?";
        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, newHashConfirmPassword);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        }
    }
}