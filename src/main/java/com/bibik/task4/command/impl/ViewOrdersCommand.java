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

public class ViewOrdersCommand implements Command {

    private static final Logger LOG = LogManager.getLogger(ViewOrdersCommand.class);
    private static final String ORDERS_PAGE = "/WEB-INF/jsp/orders.jsp";
    private static final String REDIRECT_LOGIN = "redirect:/controller?command=login";
    private static final String ERROR_PAGE = "/WEB-INF/jsp/error.jsp";
    private static final String ERROR_ATTRIBUTE = "error";

    private final OrderService orderService;

    public ViewOrdersCommand() {
        this.orderService = new OrderServiceImpl();
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            LOG.warn("Unauthenticated orders view attempt");
            return REDIRECT_LOGIN;
        }

        User user = (User) session.getAttribute("user");
        LOG.debug("Viewing orders for user: {}", user.getLogin());

        try {
            request.setAttribute("orders", orderService.getByUserId(user.getId()));
            return ORDERS_PAGE;
        } catch (ServiceException e) {
            LOG.error("ServiceException while loading orders for user: {}", user.getLogin(), e);
            request.setAttribute(ERROR_ATTRIBUTE, "Cannot load orders: " + e.getMessage());
            return ERROR_PAGE;
        }
    }
}
