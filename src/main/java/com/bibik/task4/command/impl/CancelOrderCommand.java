package com.bibik.task4.command.impl;

import com.bibik.task4.command.Command;
import com.bibik.task4.entity.User;
import com.bibik.task4.exception.ServiceException;
import com.bibik.task4.service.OrderService;
import com.bibik.task4.service.impl.OrderServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CancelOrderCommand implements Command {

    private static final Logger LOG = LogManager.getLogger(CancelOrderCommand.class);

    private static final String REDIRECT_PREFIX = "redirect:";
    private static final String REDIRECT_ORDERS = REDIRECT_PREFIX + "/controller?command=viewOrders";
    private static final String REDIRECT_LOGIN = REDIRECT_PREFIX + "/controller?command=login";
    private static final String ERROR_ATTRIBUTE = "error";

    private final OrderService orderService;

    public CancelOrderCommand() {
        this.orderService = new OrderServiceImpl();
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            LOG.warn("Unauthenticated cancel attempt");
            return REDIRECT_LOGIN;
        }

        User user = (User) session.getAttribute("user");
        int orderId;

        try {
            orderId = Integer.parseInt(request.getParameter("orderId"));
        } catch (NumberFormatException e) {
            LOG.error("Invalid order id", e);
            request.setAttribute(ERROR_ATTRIBUTE, "Invalid order id");
            return REDIRECT_ORDERS;
        }

        LOG.info("Cancelling order: orderId={}, userId={}", orderId, user.getId());

        try {
            boolean cancelled = orderService.cancel(orderId, user.getId());
            if (!cancelled) {
                LOG.warn("Order cancellation failed: {}", orderId);
                request.setAttribute(ERROR_ATTRIBUTE, "Cannot cancel order");
            } else {
                LOG.info("Order cancelled successfully: {}", orderId);
            }
        } catch (ServiceException e) {
            LOG.error("ServiceException while cancelling order: {}", orderId, e);
            request.setAttribute(ERROR_ATTRIBUTE, "Cancellation error: " + e.getMessage());
        }

        return REDIRECT_ORDERS;
    }
}