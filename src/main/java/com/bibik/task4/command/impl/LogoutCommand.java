package com.bibik.task4.command.impl;

import com.bibik.task4.command.Command;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogoutCommand implements Command {

    private static final Logger LOG = LogManager.getLogger(LogoutCommand.class);
    private static final String REDIRECT_LOGIN = "redirect:/controller?command=login";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            LOG.info("User logging out: {}", session.getAttribute("user"));
            session.invalidate();
        }
        return REDIRECT_LOGIN;
    }
}