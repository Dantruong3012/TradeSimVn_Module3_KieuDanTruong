package com.stock_market_simulator.stock_market_simulator.controller.MarketController;

import com.stock_market_simulator.stock_market_simulator.dto.UserDto;
import com.stock_market_simulator.stock_market_simulator.model.entity.Order;
import com.stock_market_simulator.stock_market_simulator.services.impl.PlaceOrderService;
import com.stock_market_simulator.stock_market_simulator.services.impl.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet(name = "order", value = "/order-place")
public class PlaceOrderController extends HttpServlet {
    private PlaceOrderService placeOrderService = new PlaceOrderService();
    private UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        UserDto userDto = (UserDto) session.getAttribute("account");

        if (userDto == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        String symbol = req.getParameter("symbol");
        String orderType = req.getParameter("orderType");
        String side = req.getParameter("side");
        String quantityStr = req.getParameter("quantity");
        String priceStr = req.getParameter("price");

        try {
            if (symbol == null || symbol.trim().isEmpty() ||
                    quantityStr == null || quantityStr.trim().isEmpty()) {
                redirectWithError(session, resp, "Dữ liệu Symbol/Khối lượng không được để trống!");
                return;
            }

            int qty = Integer.parseInt(quantityStr);
            if (qty <= 0) {
                redirectWithError(session, resp, "Khối lượng phải lớn hơn 0!");
                return;
            }
            if (qty % 100 != 0) {
                redirectWithError(session, resp, "Khối lượng phải là bội số của 100!");
                return;
            }

            BigDecimal price = BigDecimal.ZERO;

            if ("LO".equalsIgnoreCase(orderType)) {
                if (priceStr == null || priceStr.trim().isEmpty()) {
                    redirectWithError(session, resp, "Vui lòng nhập giá cho lệnh LO!");
                    return;
                }
                price = new BigDecimal(priceStr);
                if (price.compareTo(BigDecimal.ZERO) <= 0) {
                    redirectWithError(session, resp, "Giá đặt lệnh phải lớn hơn 0!");
                    return;
                }
            } else if ("MP".equalsIgnoreCase(orderType)) {
                price = BigDecimal.ZERO;
            } else {
                redirectWithError(session, resp, "Loại lệnh không hợp lệ!");
                return;
            }

            if (!"BUY".equals(side) && !"SELL".equals(side)) {
                redirectWithError(session, resp, "Loại giao dịch không hợp lệ!");
                return;
            }

            Order newOrder = new Order();
            newOrder.setUserId(userDto.getId());
            newOrder.setSymbol(symbol.toUpperCase());
            newOrder.setSide(side);
            newOrder.setOrderType(orderType);
            newOrder.setQuantity(qty);
            newOrder.setPrice(price);
            newOrder.setStatus("PENDING");

            boolean success = placeOrderService.orderProcessing(newOrder);

            if (success) {
                try {
                    UserDto updatedUser = userService.findUserById(userDto.getId());
                    session.setAttribute("account", updatedUser);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                session.setAttribute("msgSuccess", "Đặt lệnh thành công!");
            } else {
                session.setAttribute("msgError", "Đặt lệnh thất bại! Kiểm tra số dư, cổ phiếu hoặc mã CK.");
            }

        } catch (NumberFormatException e) {
            redirectWithError(session, resp, "Dữ liệu nhập vào không đúng định dạng số!");
            return;
        } catch (Exception e) {
            e.printStackTrace();
            redirectWithError(session, resp, "Lỗi hệ thống: " + e.getMessage());
            return;
        }

        resp.sendRedirect("trading");
    }

    public void redirectWithError(HttpSession session, HttpServletResponse resp, String message) throws IOException {
        session.setAttribute("msgError", message);
        resp.sendRedirect("trading");
    }
}