package com.stock_market_simulator.stock_market_simulator.controller.message_status_controller;

import com.stock_market_simulator.stock_market_simulator.dto.UserDto;
import com.stock_market_simulator.stock_market_simulator.services.impl.NotificationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "readAll", value = "/read-all")
public class MarkAsReadController extends HttpServlet {
    private NotificationService notifiService = new NotificationService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        UserDto userDto = (UserDto) session.getAttribute("account");

        if (userDto != null){
            notifiService.markAsRead(userDto.getId());
        }

        // load lai trang cho mat dau cham do

        String referer = req.getHeader("referer");
        resp.sendRedirect(referer != null ? referer : "home");
    }
}
