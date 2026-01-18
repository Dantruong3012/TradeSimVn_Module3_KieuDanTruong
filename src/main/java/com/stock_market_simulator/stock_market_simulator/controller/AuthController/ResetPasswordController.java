package com.stock_market_simulator.stock_market_simulator.controller.AuthController;

import com.stock_market_simulator.stock_market_simulator.dao.UserDao;
import com.stock_market_simulator.stock_market_simulator.services.impl.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "reset-password", value = "/reset-password")
public class ResetPasswordController extends HttpServlet {
    private UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       req.getRequestDispatcher("/forgot_password.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = req.getParameter("user_name");
        String securityCode = req.getParameter("security_code");
        String newPassword = req.getParameter("new_password");
        String confirmPassword = req.getParameter("confirm_password");

        String error = null;
        if (userName == null || userName.trim().isEmpty()) {
            error = "The user name must not be empty!";
        } else if (newPassword == null || newPassword.trim().isEmpty()) {
            error = "Password must not be empty!";
        } else if (confirmPassword == null || !confirmPassword.equals(newPassword)){
            error = "The verification password doesn't match!";
        }
        if (error != null) {
            req.setAttribute("error", error);
            req.setAttribute("oldUserName", userName);
            req.setAttribute("oldSecurityCode", securityCode);
            req.getRequestDispatcher("/forgot_password.jsp").forward(req,resp);
            return;
        }

        boolean isResetSuccess = userService.resetPassword(userName, securityCode, newPassword);

        if (isResetSuccess){
            req.getSession().setAttribute("successMessage", "Password changed successfully. Please log in again!");
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
        }else {
            req.setAttribute("error", "Incorrect username or security code!");
            req.getRequestDispatcher("/forgot_password.jsp").forward(req,resp);
        }


    }
}
