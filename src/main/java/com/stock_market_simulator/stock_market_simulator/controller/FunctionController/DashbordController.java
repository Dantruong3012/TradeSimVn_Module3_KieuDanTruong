package com.stock_market_simulator.stock_market_simulator.controller.FunctionController;

import com.stock_market_simulator.stock_market_simulator.dto.PortfolioDto;
import com.stock_market_simulator.stock_market_simulator.dto.UserDto;
import com.stock_market_simulator.stock_market_simulator.model.entity.Notification;
import com.stock_market_simulator.stock_market_simulator.services.impl.NotificationService;
import com.stock_market_simulator.stock_market_simulator.services.impl.PortfolioService;
import com.stock_market_simulator.stock_market_simulator.services.impl.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "dashbord", value = "/home")
public class DashbordController extends HttpServlet {
    private final PortfolioService portfolioService = new PortfolioService();
    private final NotificationService notifiService = new NotificationService();
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            HttpSession session = req.getSession();
            UserDto userDto = (UserDto) session.getAttribute("account");

            if (userDto == null) {
                resp.sendRedirect("login.jsp");
                return;
            }

            UserDto freshUserDto = null;
            try {
                freshUserDto = userService.findUserById(userDto.getId());
            } catch (Exception e) { e.printStackTrace(); }

            if (freshUserDto != null) {
                session.setAttribute("account", freshUserDto);
            } else {
                freshUserDto = userDto;
            }

            BigDecimal currentBalance = freshUserDto.getBalance() != null ? freshUserDto.getBalance() : BigDecimal.ZERO;
            BigDecimal lockedBalance = freshUserDto.getLockedBalance() != null ? freshUserDto.getLockedBalance() : BigDecimal.ZERO;

            List<PortfolioDto> portfolioDtoList = portfolioService.getAssetIndexDetail(freshUserDto.getId());
            if (portfolioDtoList == null) portfolioDtoList = new ArrayList<>();

            BigDecimal stockValue = BigDecimal.ZERO;
            BigDecimal totalGainLoss = BigDecimal.ZERO;

            for (PortfolioDto element : portfolioDtoList) {
                if (element.getMarketValue() != null) stockValue = stockValue.add(element.getMarketValue());
                if (element.getGainLoss() != null) totalGainLoss = totalGainLoss.add(element.getGainLoss());
            }

            BigDecimal totalNav = currentBalance.add(stockValue);
            BigDecimal cashValue = totalNav.subtract(stockValue);

            double stockPercent = 0;
            double cashPercent = 100;
            if (totalNav.compareTo(BigDecimal.ZERO) > 0) {
                stockPercent = stockValue.divide(totalNav, 4, RoundingMode.HALF_UP).doubleValue() * 100;
                cashPercent = 100 - stockPercent;
            }

            double totalPerformance = 0;
            BigDecimal initialCapital = totalNav.subtract(totalGainLoss); // <-- Bạn đã tính ở đây

            if (initialCapital.compareTo(BigDecimal.ZERO) > 0) {
                totalPerformance = totalGainLoss.divide(initialCapital, 4, RoundingMode.HALF_UP).doubleValue() * 100;
            }

            BigDecimal avalibleCash = currentBalance.subtract(lockedBalance);
            BigDecimal marginRatio = new BigDecimal("0.5");
            BigDecimal buyingPower = BigDecimal.ZERO;
            if (marginRatio.compareTo(BigDecimal.ZERO) > 0) {
                buyingPower = avalibleCash.divide(marginRatio, 0, RoundingMode.DOWN);
            }

            List<Notification> notifiList = notifiService.getNotifiByUser(freshUserDto.getId());
            if (notifiList == null) notifiList = new ArrayList<>();
            long unreadCount = notifiList.stream().filter(n -> !n.isSeen()).count();

            req.setAttribute("notifications", notifiList);
            req.setAttribute("unreadCount", unreadCount);
            req.setAttribute("user", freshUserDto);
            req.setAttribute("portfolioList", portfolioDtoList);

            req.setAttribute("totalNav", totalNav);
            req.setAttribute("stockValue", stockValue);
            req.setAttribute("cashValue", cashValue);
            req.setAttribute("stockPercent", stockPercent);
            req.setAttribute("cashPercent", cashPercent);
            req.setAttribute("availableCash", avalibleCash);

            req.setAttribute("totalGainLoss", totalGainLoss);
            req.setAttribute("totalPerformance", totalPerformance);
            req.setAttribute("initialCapital", initialCapital);
            req.setAttribute("buyingPower", buyingPower);
            req.setAttribute("marginRatio", marginRatio.multiply(new BigDecimal("100")));

            req.getRequestDispatcher("/home.jsp").forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(500,"Lỗi hệ thống: " + e.getMessage());
        }
    }
}