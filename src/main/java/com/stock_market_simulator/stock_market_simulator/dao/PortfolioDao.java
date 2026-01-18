package com.stock_market_simulator.stock_market_simulator.dao;

import com.stock_market_simulator.stock_market_simulator.config.JdbcConnection;
import com.stock_market_simulator.stock_market_simulator.model.entity.Portfolio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PortfolioDao {
    // Lấy thông tin Portfolio hiện tại để tính giá vốn
    public Portfolio findByUserAndSymbol(Connection conn, int userId, String symbol) throws SQLException {
        String sql = "SELECT * FROM portfolio WHERE user_id = ? AND symbol = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, symbol);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Portfolio(
                        rs.getInt("user_id"),
                        rs.getString("symbol"),
                        rs.getInt("quantity"),
                        rs.getBigDecimal("avg_price")
                );
            }
        }
        return null;
    }

    // Insert Chỉ dùng khi User mua mã này LẦN ĐẦU TIÊN
    public void insertIntoPortfolio(Connection connection, Portfolio portfolio) throws SQLException {
        String sql = "INSERT INTO portfolio (user_id, symbol, quantity, avg_price) VALUES (?,?,?,?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1, portfolio.getUserId());
            ps.setString(2, portfolio.getSymbol());
            ps.setInt(3, portfolio.getQuantity());
            ps.setBigDecimal(4, portfolio.getAvgPrice());
            ps.executeUpdate();
        }
    }

    // Update Dùng khi mua thêm hoặc bán bớt
    public void updateToPortfolio(Connection connection, Portfolio portfolio) throws SQLException {
        String sql = "UPDATE portfolio SET avg_price = ?, quantity = ? WHERE symbol = ? AND user_id = ?";
        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setBigDecimal(1, portfolio.getAvgPrice()); // Giá vốn mới (đã tính ở Service)
            ps.setInt(2, portfolio.getQuantity());       // Tổng số lượng mới
            ps.setString(3, portfolio.getSymbol());
            ps.setInt(4, portfolio.getUserId());
            ps.executeUpdate();
        }
    }

    // lay danh sach co phieu de tinh toan chi so lien quan
    public List<Portfolio> findByUserId(Connection connection, int userId) throws SQLException {
        List<Portfolio> list = new ArrayList<>();
        String sql = "SELECT * FROM portfolio WHERE user_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Portfolio p = new Portfolio(
                        rs.getInt("user_id"),
                        rs.getString("symbol"),
                        rs.getInt("quantity"),
                        rs.getBigDecimal("avg_price")
                );
                list.add(p);
            }
        }
        return list;
    }

    // dung khi ban het co phieu trong doanh muc
    public void deletePortfolio(Connection connection, int userId, String symbol) throws SQLException {
        String sql = "DELETE FROM portfolio WHERE user_id = ? AND symbol = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, symbol);
            ps.executeUpdate();
        }
    }


}