package com.stock_market_simulator.stock_market_simulator.controller.AuthController;

import com.stock_market_simulator.stock_market_simulator.dto.UserDto;
import com.stock_market_simulator.stock_market_simulator.services.impl.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "login", value = "/login-form")
public class LoginController extends HttpServlet {
    private   UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      String userName =  req.getParameter("user_name");
      String userPassword =  req.getParameter("user_password");
        try {
            UserDto userDto = userService.login(userName, userPassword);
            if (userDto != null){
                HttpSession session = req.getSession();
                session.setAttribute("account", userDto);
                resp.sendRedirect(req.getContextPath() + "/home");
            }else{
                req.setAttribute("error", "Login failed, please double check your account information.");
                req.getRequestDispatcher("/login.jsp").forward(req,resp);
            }
        } catch (SQLException e) {
           e.printStackTrace();
        }

    }
}
