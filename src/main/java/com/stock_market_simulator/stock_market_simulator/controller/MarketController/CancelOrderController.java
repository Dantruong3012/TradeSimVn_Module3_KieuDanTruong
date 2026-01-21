package com.stock_market_simulator.stock_market_simulator.controller.MarketController;

import com.stock_market_simulator.stock_market_simulator.config.JdbcConnection;
import com.stock_market_simulator.stock_market_simulator.dao.OrderDao;
import com.stock_market_simulator.stock_market_simulator.dto.UserDto;
import com.stock_market_simulator.stock_market_simulator.model.entity.Order;
import com.stock_market_simulator.stock_market_simulator.services.impl.CancelOrderService;
import com.stock_market_simulator.stock_market_simulator.services.impl.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet(name = "cancel-order", value = "/cancel-order")
public class CancelOrderController extends HttpServlet {
    private OrderDao orderDao = new OrderDao();
    private  CancelOrderService cancelOrderService = new CancelOrderService();
    private  UserService userService = new UserService();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        UserDto userDto = (UserDto) session.getAttribute("account");
        if (userDto == null){
            resp.sendRedirect("login.jsp");
            return;
        }
       try {
           String orderIdStr = req.getParameter("orderId");
           String symbol = req.getParameter("symbol");
           if (orderIdStr == null || symbol == null) {
               session.setAttribute("msgError", "Dữ liệu không hợp lệ!");
               resp.sendRedirect("order-book?status=PENDING");
               return;
           }
           int orderId = Integer.parseInt(orderIdStr);
           Order targetOrder = null;
           try (Connection connection = JdbcConnection.getConnection()){
               targetOrder = orderDao.findOrderById(connection, orderId);
           }catch (SQLException ex){ex.printStackTrace();}

           if (targetOrder == null) {
               session.setAttribute("msgError", "Lệnh không tồn tại hoặc đã bị xóa!");
               resp.sendRedirect("order-book?status=PENDING");
               return;
           }

           String result = cancelOrderService.cancelOrder(symbol, targetOrder, userDto.getId());
           if ("SUCCESS".equals(result)) {
               UserDto freshUser = userService.findUserById(userDto.getId());
               session.setAttribute("account", freshUser);

               session.setAttribute("msgSuccess", "Hủy lệnh " + symbol + " thành công!");
           } else {
               session.setAttribute("msgError", result);
           }
       }catch (Exception e){
           session.setAttribute("msgError", "Lỗi hệ thống: " + e.getMessage());
           e.printStackTrace();
       }
        resp.sendRedirect("order-book?status=PENDING");
    }
}