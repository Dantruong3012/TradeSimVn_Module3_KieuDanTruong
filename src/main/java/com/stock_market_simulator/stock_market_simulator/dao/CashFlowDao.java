package com.stock_market_simulator.stock_market_simulator.dao;

import com.stock_market_simulator.stock_market_simulator.config.JdbcConnection;
import com.stock_market_simulator.stock_market_simulator.model.entity.CashFlow;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class CashFlowDao {
    public  boolean insertInCashFlow(Connection connection ,CashFlow cashFlow) throws SQLException {
        String sql = "INSERT INTO cash_flow (user_id, amount, balance_after, flow_type, description) VALUES (?,?,?,?,?)";
        try(PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, cashFlow.getUserId());
            ps.setBigDecimal(2, cashFlow.getAmount());
            ps.setBigDecimal(3, cashFlow.getBalanceAfter());
            ps.setString(4, cashFlow.getFlowType());
            ps.setString(5, cashFlow.getDescription());
            return ps.executeUpdate() > 0;
        }
    }
}
