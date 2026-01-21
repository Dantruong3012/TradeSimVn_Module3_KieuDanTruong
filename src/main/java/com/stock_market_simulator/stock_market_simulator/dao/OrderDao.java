package com.stock_market_simulator.stock_market_simulator.dao;

import com.stock_market_simulator.stock_market_simulator.dto.OrderDto;
import com.stock_market_simulator.stock_market_simulator.model.entity.Order;
import org.eclipse.tags.shaded.org.apache.xpath.operations.Or;
// import jakarta.servlet.jsp.tagext.TryCatchFinally; // Xóa import thừa này đi

import java.math.BigDecimal;
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
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
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
    public void updateAfterOrder(Connection connection, int orderId, int matchedQty, String newStatus) throws SQLException {
        String sql = "UPDATE orders SET matched_qty = ?, status =?  WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
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

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, symbol);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
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
        if ("SELL".equals(side)) {
            sortPrice = "ASC";
        }

        String sql = "SELECT * FROM orders WHERE symbol = ? AND side = ? AND status IN ('PENDING', 'PARTIAL') " +
                "ORDER BY price " + sortPrice + ", created_time ASC";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, symbol);
            ps.setString(2, side);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
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

        if ("SELL".equals(side)) {
            sortPrice = "ASC";
        }

        String sql = "SELECT price, SUM(qty - matched_qty) as total_vol " +
                "FROM orders " +
                "WHERE symbol = ? AND side = ? AND status IN ('PENDING', 'PARTIAL') " +
                "GROUP BY price " +
                "ORDER BY price " + sortPrice + " " +
                "LIMIT 3";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, symbol);
            ps.setString(2, side);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
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


    // lay danh sach so lenh cua user

    public List<Order> findOrderByUserId(Connection connection, int userId) throws SQLException {
        List<Order> orderList = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE user_id = ? ORDER BY created_time DESC LIMIT 20";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Order order = new Order();
                order.setId(rs.getInt("id"));
                order.setUserId(rs.getInt("user_id"));
                order.setSymbol(rs.getString("symbol"));
                order.setOrderType(rs.getString("order_type"));
                order.setSide(rs.getString("side"));
                order.setStatus(rs.getString("status"));
                order.setPrice(rs.getBigDecimal("price"));
                order.setQuantity(rs.getInt("qty")); // Lưu ý tên cột DB là 'qty'
                order.setMatchedQty(rs.getInt("matched_qty"));
                if (rs.getTimestamp("created_time") != null) {
                    order.setCreatedTime(rs.getTimestamp("created_time").toLocalDateTime());
                }
                orderList.add(order);
            }

        }
        return orderList;
    }

    // lay mot co phieu cau nguoi dung cho lenh chinh sua

    public Order findOrderById(Connection connection, int orderId) throws SQLException {
        String sql = "SELECT * FROM orders WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Order order = new Order();
                order.setId(rs.getInt("id")); // QUAN TRỌNG: Phải lấy ID
                order.setUserId(rs.getInt("user_id"));
                order.setSymbol(rs.getString("symbol"));
                order.setOrderType(rs.getString("order_type"));
                order.setSide(rs.getString("side"));
                order.setStatus(rs.getString("status"));
                order.setPrice(rs.getBigDecimal("price"));
                order.setQuantity(rs.getInt("qty"));
                order.setMatchedQty(rs.getInt("matched_qty"));

                if (rs.getTimestamp("created_time") != null) {
                    order.setCreatedTime(rs.getTimestamp("created_time").toLocalDateTime());
                }
                return order;
            }
            return null;
        }
    }

    public  boolean  updateOrder(Connection connection, int orderId, int userId, String symbol, BigDecimal newPrice, int newQty) throws  SQLException{
        String sql = "UPDATE orders SET price = ?, qty = ? WHERE id = ? AND user_id = ? AND symbol = ? AND status = 'PENDING'";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setBigDecimal(1, newPrice);
            ps.setInt(2, newQty);
            ps.setInt(3, orderId);
            ps.setInt(4, userId);
            ps.setString(5, symbol);
            return ps.executeUpdate() > 0;
        }
    }

}