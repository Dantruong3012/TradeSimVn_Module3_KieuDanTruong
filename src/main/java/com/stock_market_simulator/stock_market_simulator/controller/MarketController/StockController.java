package com.stock_market_simulator.stock_market_simulator.controller.MarketController;

import com.stock_market_simulator.stock_market_simulator.config.JdbcConnection;
import com.stock_market_simulator.stock_market_simulator.dao.OrderDao;
import com.stock_market_simulator.stock_market_simulator.dto.UserDto;
import com.stock_market_simulator.stock_market_simulator.model.entity.Order;
import com.stock_market_simulator.stock_market_simulator.model.entity.Stock;
import com.stock_market_simulator.stock_market_simulator.services.impl.StockService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
@WebServlet(name = "stock-controller", value = "/trading")
public class StockController extends HttpServlet {
    private StockService stockService = new StockService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            HttpSession session = req.getSession();
            UserDto userDto = (UserDto) session.getAttribute("account");
            if (userDto == null) {
                resp.sendRedirect("login.jsp");
                return;
            }

            List<Stock> marketList = stockService.getStockMarket();

            req.setAttribute("marketList", marketList);

            req.getRequestDispatcher("/trading.jsp").forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi hệ thống khi tải bảng giá.");
        }
    }
}