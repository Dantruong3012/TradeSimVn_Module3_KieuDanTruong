package com.stock_market_simulator.stock_market_simulator.dao;

import com.stock_market_simulator.stock_market_simulator.dto.OrderDto;
import com.stock_market_simulator.stock_market_simulator.model.entity.Order;
// import jakarta.servlet.jsp.tagext.TryCatchFinally; // Xóa import thừa này đi

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDao {

    // INSERT
    public void insertIntoOrder(Connection connection, Order order) throws SQLException {
        String sql = "INSERT INTO orders (user_id, symbol, order_type, side, status, price, qty) VALUES (?,?,?,?,?,?,?)";
        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1, order.getUserId());
            ps.setString(2, order.getSymbol());
            ps.setString(3, order.getOrderType());
            ps.setString(4, order.getSide());
            ps.setString(5, order.getStatus());
            ps.setBigDecimal(6, order.getPrice());
            ps.setInt(7, order.getQuantity());
            ps.executeUpdate();
        }
    }

    //UPDATE
    public void updateAfterOrder(Connection connection, int orderId, int matchedQty, String newStatus) throws SQLException{
        String sql = "UPDATE orders SET matched_qty = ?, status =?  WHERE id = ?";
        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1, matchedQty);
            ps.setString(2, newStatus);
            ps.setInt(3, orderId);
            ps.executeUpdate();
        }
    }

    public List<OrderDto> getTotalMarketVolumeOfSymbol(Connection connection, String symbol) throws SQLException { // Thêm throws SQLException
        List<OrderDto> list = new ArrayList<>();
        String sql = "SELECT side, SUM(qty - matched_qty) AS total_vol " +
                "FROM orders " +
                "WHERE symbol = ? AND status IN ('PENDING', 'PARTIAL') " +
                "GROUP BY side";

        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, symbol);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                OrderDto orderDto = new OrderDto();
                orderDto.setSymbol(symbol);
                orderDto.setSide(rs.getString("side"));
                orderDto.setTotalVolume(rs.getInt("total_vol"));
                list.add(orderDto);
            }
        }
        return list;
    }

    public List<Order> getPendingOrderForMatching(Connection connection, String symbol, String side) throws SQLException {
        List<Order> list = new ArrayList<>();

        String sortPrice = "DESC";
        if ("SELL".equals(side)){
            sortPrice = "ASC";
        }

        String sql = "SELECT * FROM orders WHERE symbol = ? AND side = ? AND status IN ('PENDING', 'PARTIAL') " +
                "ORDER BY price " + sortPrice + ", created_time ASC";

        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, symbol);
            ps.setString(2, side);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Order order = new Order();
                order.setId(rs.getInt("id"));
                order.setUserId(rs.getInt("user_id"));
                order.setSymbol(rs.getString("symbol"));
                order.setSide(rs.getString("side"));
                order.setOrderType(rs.getString("order_type"));
                order.setStatus(rs.getString("status"));
                order.setPrice(rs.getBigDecimal("price"));
                order.setQuantity(rs.getInt("qty"));
                order.setMatchedQty(rs.getInt("matched_qty"));

                if (rs.getTimestamp("created_time") != null) {
                    order.setCreatedTime(rs.getTimestamp("created_time").toLocalDateTime());
                }
                list.add(order);
            }
        }
        return list;
    }

    public List<OrderDto> getTopThreeGoodPrice(Connection connection, String symbol, String side) throws SQLException {
        List<OrderDto> list = new ArrayList<>();
        String sortPrice = "DESC";

        if ("SELL".equals(side)){
            sortPrice = "ASC";
        }

        String sql = "SELECT price, SUM(qty - matched_qty) as total_vol " +
                "FROM orders " +
                "WHERE symbol = ? AND side = ? AND status IN ('PENDING', 'PARTIAL') " +
                "GROUP BY price " +
                "ORDER BY price " + sortPrice + " " +
                "LIMIT 3";

        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, symbol);
            ps.setString(2, side);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                OrderDto dto = new OrderDto();
                dto.setSymbol(symbol);
                dto.setSide(side);
                dto.setPrice(rs.getBigDecimal("price"));
                dto.setTotalVolume(rs.getInt("total_vol")); // Lấy đúng tên alias

                list.add(dto);
            }
        }
        return list;
    }
}