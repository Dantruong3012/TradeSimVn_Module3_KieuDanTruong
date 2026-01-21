package com.stock_market_simulator.stock_market_simulator.controller.MarketController;

import com.stock_market_simulator.stock_market_simulator.dto.UserDto;
import com.stock_market_simulator.stock_market_simulator.services.impl.EditOrderService;
import com.stock_market_simulator.stock_market_simulator.services.impl.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet(name = "edit-controller", value = "/edit-order")
public class EditOrderController extends HttpServlet {
    private EditOrderService editOrderService = new EditOrderService();
    private UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();
        UserDto userDto = (UserDto) session.getAttribute("account");

        if (userDto == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        try {
            String orderIdStr = req.getParameter("orderId");
            String symbol = req.getParameter("symbol");
            String newPriceStr = req.getParameter("newPrice");
            String newQtyStr = req.getParameter("newQty");
            String orderType = req.getParameter("orderType");

            if (orderIdStr == null || newQtyStr == null || symbol == null) {
                session.setAttribute("msgError", "Dữ liệu không hợp lệ!");
                resp.sendRedirect("order-book?status=PENDING");
                return;
            }

            int orderId = Integer.parseInt(orderIdStr);
            int qty = Integer.parseInt(newQtyStr);
            BigDecimal price = BigDecimal.ZERO;

            if ("MP".equalsIgnoreCase(orderType)) {
                price = BigDecimal.ZERO;
            } else {
                if (newPriceStr == null || newPriceStr.trim().isEmpty()) {
                    session.setAttribute("msgError", "Vui lòng nhập giá!");
                    resp.sendRedirect("order-book?status=PENDING");
                    return;
                }
                price = new BigDecimal(newPriceStr);

                if (price.compareTo(BigDecimal.ZERO) <= 0) {
                    session.setAttribute("msgError", "Giá phải lớn hơn 0!");
                    resp.sendRedirect("order-book?status=PENDING");
                    return;
                }
            }

            if (qty <= 0 || qty % 100 != 0) {
                session.setAttribute("msgError", "Khối lượng phải là bội số của 100!");
                resp.sendRedirect("order-book?status=PENDING");
                return;
            }

            String result = editOrderService.processOrderEdit(orderId, userDto.getId(), symbol, qty, price);

            if ("SUCCESS".equals(result)) {
                UserDto freshUser = userService.findUserById(userDto.getId());
                session.setAttribute("account", freshUser);
                session.setAttribute("msgSuccess", "Cập nhật lệnh " + symbol + " thành công!");
            } else {
                session.setAttribute("msgError", result);
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
            session.setAttribute("msgError", "Lỗi định dạng số! (Kiểm tra giá hoặc khối lượng)");
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("msgError", "Lỗi hệ thống: " + e.getMessage());
        }
        resp.sendRedirect("order-book?status=PENDING");
    }
}