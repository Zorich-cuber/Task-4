package com.bibik.task4.command.impl;

import com.bibik.task4.command.Command;
import com.bibik.task4.entity.User;
import com.bibik.task4.exception.ServiceException;
import com.bibik.task4.exception.ValidationException;
import com.bibik.task4.service.UserService;
import com.bibik.task4.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RegistrationCommand implements Command {

    private static final Logger LOG = LogManager.getLogger(RegistrationCommand.class);
    private static final String REGISTRATION_PAGE = "/WEB-INF/jsp/registration.jsp";
    private static final String LOGIN_PAGE = "/WEB-INF/jsp/login.jsp";
    private static final String ERROR_ATTRIBUTE = "error";
    private static final String MESSAGE_ATTRIBUTE = "message";

    private final UserService userService;

    public RegistrationCommand() {
        this.userService = new UserServiceImpl();
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String email = request.getParameter("email");

        LOG.info("Registration attempt: login={}, email={}", login, email);

        try {
            User user = userService.register(login, password, email);
            LOG.info("User registered successfully: {}", user.getLogin());
            request.setAttribute(MESSAGE_ATTRIBUTE, "Registration successful. Please login.");
            return LOGIN_PAGE;
        } catch (ValidationException e) {
            LOG.warn("Validation error during registration: {}", e.getMessage());
            request.setAttribute(ERROR_ATTRIBUTE, e.getMessage());
            return REGISTRATION_PAGE;
        } catch (ServiceException e) {
            LOG.error("ServiceException during registration", e);
            request.setAttribute(ERROR_ATTRIBUTE, "Registration error: " + e.getMessage());
            return REGISTRATION_PAGE;
        }
    }
}
