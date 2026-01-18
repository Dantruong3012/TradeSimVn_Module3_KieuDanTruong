package com.stock_market_simulator.stock_market_simulator.dao;

import com.stock_market_simulator.stock_market_simulator.dto.StockHistoryDto;
import com.stock_market_simulator.stock_market_simulator.dto.TransactionDto;
import com.stock_market_simulator.stock_market_simulator.model.entity.Transaction;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TransactionDao {

    // INSERT
    public void insertIntoTransaction(Connection connection, Transaction transaction) throws SQLException {
        String sql = "INSERT INTO transactions (buy_order_id, sell_order_id, symbol, price, volume) VALUES (?,?,?,?,?)";
        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1, transaction.getBuyOrderId());
            ps.setInt(2, transaction.getSellOrderId());
            ps.setString(3, transaction.getSymbol());
            ps.setBigDecimal(4, transaction.getPrice());
            ps.setInt(5, transaction.getVolume());
            ps.executeUpdate();
        }
    }

    // LẤY LỊCH SỬ GIAO DỊCH CỦA USER
    public List<TransactionDto> getTransactionByUserId(Connection connection, int userId) throws SQLException {
        List<TransactionDto> list = new ArrayList<>();
        String sql = "SELECT t.id, t.symbol, t.price, t.volume, t.created_time, " +
                "o_buy.user_id as buyer_id, o_sell.user_id as seller_id " +
                "FROM transactions t " +
                "LEFT JOIN orders o_buy ON t.buy_order_id = o_buy.id " +
                "LEFT JOIN orders o_sell ON t.sell_order_id = o_sell.id " +
                "WHERE o_buy.user_id = ? OR o_sell.user_id = ? " +
                "ORDER BY t.created_time DESC";

        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1, userId);
            ps.setInt(2, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                TransactionDto transactionDto = new TransactionDto();
                transactionDto.setSymbol(rs.getString("symbol"));
                transactionDto.setPrice(rs.getBigDecimal("price"));
                transactionDto.setVolume(rs.getInt("volume"));

                if (rs.getTimestamp("created_time") != null) {
                    transactionDto.setCreatedTime(rs.getTimestamp("created_time").toLocalDateTime());
                }

                int buyerId = rs.getInt("buyer_id");
                if (buyerId == userId) {
                    transactionDto.setSide("BUY");
                } else {
                    transactionDto.setSide("SELL");
                }

                list.add(transactionDto);
            }
        }
        return list;
    }


    // LẤY DATA VẼ BIỂU ĐỒ
    public List<StockHistoryDto> getTransactionBySymbol(Connection connection, String symbol) throws SQLException {
        List<StockHistoryDto> list = new ArrayList<>();

        // Lấy 50 giao dịch gần nhất để vẽ biểu đồ
        String sql = "SELECT created_time, price FROM transactions WHERE symbol = ? ORDER BY created_time DESC LIMIT 50";

        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, symbol);
            ResultSet rs = ps.executeQuery();

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

            while (rs.next()){
                StockHistoryDto stockHistoryDto = new StockHistoryDto();

                stockHistoryDto.setPrice(rs.getBigDecimal("price"));
                Timestamp ts = rs.getTimestamp("created_time");
                if (ts != null) {
                    stockHistoryDto.setTimeStr(sdf.format(ts));
                }

                list.add(stockHistoryDto);
            }
        }
        Collections.reverse(list);
        return list;
    }
}