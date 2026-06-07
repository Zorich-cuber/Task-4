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

public class CreateOrderCommand implements Command {

    private static final Logger LOG = LogManager.getLogger(CreateOrderCommand.class);

    private static final String REDIRECT_PREFIX = "redirect:";
    private static final String REDIRECT_LOGIN = REDIRECT_PREFIX + "/controller?command=login";
    private static final String REDIRECT_ORDERS = REDIRECT_PREFIX + "/controller?command=viewOrders";
    private static final String PRODUCTS_PAGE = "/WEB-INF/jsp/products.jsp";
    private static final String ERROR_ATTRIBUTE = "error";

    private final OrderService orderService;

    public CreateOrderCommand() {
        this.orderService = new OrderServiceImpl();
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            LOG.warn("Unauthenticated order attempt");
            return REDIRECT_LOGIN;
        }

        User user = (User) session.getAttribute("user");
        int productId;
        int quantity;

        try {
            productId = Integer.parseInt(request.getParameter("productId"));
            quantity = Integer.parseInt(request.getParameter("quantity"));
        } catch (NumberFormatException e) {
            LOG.error("Invalid product id or quantity", e);
            request.setAttribute(ERROR_ATTRIBUTE, "Invalid product or quantity");
            return PRODUCTS_PAGE;
        }

        LOG.info("Creating order: userId={}, productId={}, quantity={}",
                user.getId(), productId, quantity);

        try {
            orderService.create(user.getId(), productId, quantity);
            LOG.info("Order created successfully for user: {}", user.getLogin());
            return REDIRECT_ORDERS;
        } catch (ServiceException e) {
            LOG.error("ServiceException while creating order", e);
            request.setAttribute(ERROR_ATTRIBUTE, "Order creation failed: " + e.getMessage());
            return PRODUCTS_PAGE;
        }
    }
}