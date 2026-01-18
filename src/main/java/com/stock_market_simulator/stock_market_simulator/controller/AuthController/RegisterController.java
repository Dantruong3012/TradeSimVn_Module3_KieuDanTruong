package com.stock_market_simulator.stock_market_simulator.controller.AuthController;

import com.stock_market_simulator.stock_market_simulator.model.entity.User;
import com.stock_market_simulator.stock_market_simulator.services.impl.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "register", value = "/register")

public class RegisterController extends HttpServlet {
   private UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = req.getParameter("user_name");
        String disPlayName = req.getParameter("display_name");
        String userPassword = req.getParameter("user_password");
        String confirmPassword = req.getParameter("confirm_password");
        String securityCode= req.getParameter("security_code");

        if (confirmPassword != null && !confirmPassword.equals(userPassword)) {
            req.setAttribute("error", "The verification password doesn't match!");
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
            return;
        }

        if (userName ==  null || userService.isUserExists(userName)) {
            req.setAttribute("error", "User Name'" + userName + "' Has Been Used!");
            req.setAttribute("oldName", disPlayName);
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
            return;
        }

        User newUser = new User(userName, disPlayName, userPassword, confirmPassword, securityCode);
        boolean success =  userService.register(newUser);

        if (success){
            HttpSession session = req.getSession();
            session.setAttribute("successMessage", "Registration successful! Please log in.");
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
        }else {
            req.setAttribute("error", "Registration failed due to a system error. Please try again!");
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
        }


    }
}
