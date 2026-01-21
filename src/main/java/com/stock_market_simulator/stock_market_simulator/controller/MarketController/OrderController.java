package com.stock_market_simulator.stock_market_simulator.controller.MarketController;

import com.stock_market_simulator.stock_market_simulator.config.JdbcConnection;
import com.stock_market_simulator.stock_market_simulator.dao.NotificationDao;
import com.stock_market_simulator.stock_market_simulator.dao.OrderDao;
import com.stock_market_simulator.stock_market_simulator.dto.UserDto;
import com.stock_market_simulator.stock_market_simulator.model.entity.Notification;
import com.stock_market_simulator.stock_market_simulator.model.entity.Order;
import com.stock_market_simulator.stock_market_simulator.services.impl.NotificationService;
import com.stock_market_simulator.stock_market_simulator.services.impl.UserService; // Import UserService
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "order-book-controller", value = "/order-book")
public class OrderController extends HttpServlet {

    private OrderDao orderDao = new OrderDao();
    private UserService userService = new UserService();
    private NotificationService notificationService = new NotificationService();
    private NotificationDao notificationDao = new NotificationDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            HttpSession session = req.getSession();
            UserDto userDto = (UserDto) session.getAttribute("account");


            if (userDto == null) {
                resp.sendRedirect("login.jsp");
                return;
            }


            try {
                UserDto freshUser = userService.findUserById(userDto.getId());
                if (freshUser != null) {
                    session.setAttribute("account", freshUser);
                    userDto = freshUser;
                    BigDecimal currentBalance = userDto.getBalance() != null ? userDto.getBalance() : BigDecimal.ZERO;
                    BigDecimal lockedBalance = userDto.getLockedBalance() != null ? userDto.getLockedBalance() : BigDecimal.ZERO;
                    BigDecimal availableCash = currentBalance.subtract(lockedBalance);
                    req.setAttribute("availableCash", availableCash);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            String status = req.getParameter("status");
            List<Order> allOrders = new ArrayList<>();
            List<Notification> notificationList = new ArrayList<>();
            try (Connection connection = JdbcConnection.getConnection()){
                allOrders = orderDao.findOrderByUserId(connection, userDto.getId());
                notificationList = notificationDao.getNotifiByUserId(connection, userDto.getId());
            } catch (SQLException e){
                e.printStackTrace();
            }

            // Lọc dữ liệu
            List<Order> filteredList;
            if (status != null && !status.isEmpty() && !status.equals("ALL")){
                filteredList = allOrders.stream()
                        .filter(o -> o.getStatus().equalsIgnoreCase(status))
                        .collect(Collectors.toList());
            } else {
                filteredList = allOrders;
            }
            req.setAttribute("orderList", filteredList);
            List<Notification> notifiList = notificationService.getNotifiByUser(userDto.getId());
            if (notifiList == null) notifiList = new ArrayList<>();
            long unreadCount = notifiList.stream().filter(n -> !n.isSeen()).count();
            req.setAttribute("notifications", notifiList);
            req.setAttribute("unreadCount", unreadCount);
            req.getRequestDispatcher("/order-book.jsp").forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}