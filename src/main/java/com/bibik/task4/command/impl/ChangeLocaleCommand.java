package com.bibik.task4.command.impl;

import com.bibik.task4.command.Command;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChangeLocaleCommand implements Command {

    private static final Logger LOG = LogManager.getLogger(ChangeLocaleCommand.class);

    private static final String REDIRECT_PREFIX = "redirect:";
    private static final String DEFAULT_REDIRECT = REDIRECT_PREFIX + "/controller?command=viewProducts";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String locale = request.getParameter("locale");
        HttpSession session = request.getSession();
        session.setAttribute("locale", locale);
        LOG.info("Locale changed to: {}", locale);

        String referer = request.getHeader("Referer");
        if (referer != null && !referer.isEmpty()) {
            LOG.debug("Redirecting to referer: {}", referer);
            return REDIRECT_PREFIX + referer;
        }

        LOG.debug("No referer, redirecting to default page");
        return DEFAULT_REDIRECT;
    }
}