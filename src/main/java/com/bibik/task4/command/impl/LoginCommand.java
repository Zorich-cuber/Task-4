package com.bibik.task4.command.impl;

import com.bibik.task4.command.Command;
import com.bibik.task4.entity.User;
import com.bibik.task4.exception.ServiceException;
import com.bibik.task4.service.UserService;
import com.bibik.task4.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoginCommand implements Command {

    private static final Logger LOG = LogManager.getLogger(LoginCommand.class);
    private static final String LOGIN_PAGE = "/WEB-INF/jsp/login.jsp";
    private static final String ERROR_ATTRIBUTE = "error";

    private final UserService userService;

    public LoginCommand() {
        this.userService = new UserServiceImpl();
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String login = request.getParameter("login");
        String password = request.getParameter("password");

        LOG.info("Login attempt for user: {}", login);

        if (login == null || password == null) {
            LOG.warn("Login or password is null");
            request.setAttribute(ERROR_ATTRIBUTE, "Login and password are required");
            return LOGIN_PAGE;
        }

        try {
            User user = userService.authenticate(login, password);
            if (user != null) {
                HttpSession session = request.getSession(true);
                session.setAttribute("user", user);
                session.setAttribute("locale", user.getLocale());
                LOG.info("User logged in successfully: {}", login);


                try {
                    response.sendRedirect(request.getContextPath() + "/controller?command=viewProducts");
                } catch (Exception e) {
                    LOG.error("Redirect failed", e);
                }
                return null;
            } else {
                LOG.warn("Authentication failed for user: {}", login);
                request.setAttribute(ERROR_ATTRIBUTE, "Invalid login or password");
                return LOGIN_PAGE;
            }
        } catch (ServiceException e) {
            LOG.error("ServiceException during login for user: {}", login, e);
            request.setAttribute(ERROR_ATTRIBUTE, "Authentication error: " + e.getMessage());
            return LOGIN_PAGE;
        }
    }
}
