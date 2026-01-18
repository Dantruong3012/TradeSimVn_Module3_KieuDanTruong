package com.stock_market_simulator.stock_market_simulator.dao;

import com.stock_market_simulator.stock_market_simulator.model.entity.Stock;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StockDao {

    // LẤY TẤT CẢ CỔ PHIẾU
    public List<Stock> findAllStocks(Connection connection) throws SQLException {
        String sql = "SELECT * FROM stocks";
        List<Stock> list = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Stock s = new Stock();

                s.setSymbol(rs.getString("symbol"));
                s.setCompanyName(rs.getString("company_name"));
                s.setExchange(rs.getString("exchange"));

                s.setCurrentPrice(rs.getBigDecimal("current_price"));
                s.setRefPrice(rs.getBigDecimal("ref_price"));
                s.setCeilingPrice(rs.getBigDecimal("ceiling_price"));
                s.setFloorPrice(rs.getBigDecimal("floor_price"));
                s.setChangePrice(rs.getBigDecimal("change_price"));
                s.setChangePercent(rs.getBigDecimal("change_percent"));
                s.setVolume(rs.getInt("volume"));

                list.add(s);
            }
        }
        return list;
    }

    // TÌM CỔ PHIẾU THEO MÃ
    public Stock findStockBySymbol(Connection connection, String symbol) throws SQLException {
        String sql = "SELECT * FROM stocks WHERE symbol = ?";

        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, symbol);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                return new Stock(rs.getString("symbol"),
                        rs.getString("company_name"),
                        rs.getBigDecimal("current_price"),
                        rs.getBigDecimal("change_price"),
                        rs.getBigDecimal("change_percent"),
                        rs.getBigDecimal("ref_price"), // Giá tham chiếu
                        rs.getBigDecimal("ceiling_price"),
                        rs.getBigDecimal("floor_price"),
                        rs.getString("exchange"));
            }
        }
        return null;
    }

    // CẬP NHẬT GIÁ SAU KHI KHỚP LỆNH (Update Price)
    public void updateStockPrice(Connection connection, String symbol, BigDecimal matchPrice, BigDecimal refPrice) throws SQLException {
        String sql = "UPDATE stocks SET current_price = ?, change_percent = ? WHERE symbol = ?";

        // Công thức: (Giá mới - Giá tham chiếu) / Giá tham chiếu * 100
        BigDecimal percent = BigDecimal.ZERO;
        if (refPrice.compareTo(BigDecimal.ZERO) != 0) {
            percent = matchPrice.subtract(refPrice)
                    .divide(refPrice, 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"));
        }

        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setBigDecimal(1, matchPrice);
            ps.setBigDecimal(2, percent);
            ps.setString(3, symbol);
            ps.executeUpdate();
        }
    }

    // LẤY GIÁ HIỆN TẠI (Dùng để tính toán tài sản ròng - NAV)
    public BigDecimal getCurrentPrice(Connection connection, String symbol) throws SQLException {
        String sql = "SELECT current_price FROM stocks WHERE symbol = ?";
        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, symbol);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                return rs.getBigDecimal("current_price");
            }
        }
        return BigDecimal.ZERO;
    }
}