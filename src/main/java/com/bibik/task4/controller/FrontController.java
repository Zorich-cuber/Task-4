package com.bibik.task4.controller;

import java.io.*;

import com.bibik.task4.command.Command;
import com.bibik.task4.command.CommandType;
import com.bibik.task4.command.Router;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet("/controller")
public class FrontController extends HttpServlet {

    private static final Logger LOG = LogManager.getLogger(FrontController.class);
    private static final String REDIRECT_PREFIX = "redirect:";
    private static final String ERROR_PAGE = "/WEB-INF/jsp/error.jsp";
    private static final String ERROR_ATTRIBUTE = "error";
    private static final String DEFAULT_COMMAND = "viewProducts";

    private final Router router;

    public FrontController() {
        this.router = new Router();
        LOG.info("FrontController created");
    }

    @Override
    public void init() throws ServletException {
        super.init();
        LOG.info("FrontController initialized");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LOG.debug("GET request received: {}", request.getRequestURI());
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LOG.debug("POST request received: {}", request.getRequestURI());
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String commandName = request.getParameter("command");
        if (commandName == null || commandName.trim().isEmpty()) {
            commandName = DEFAULT_COMMAND;
            LOG.debug("Command parameter is null/empty, using default: {}", DEFAULT_COMMAND);
        }

        CommandType commandType = CommandType.fromString(commandName);
        LOG.info("Processing command: {}", commandType.getName());

        try {
            Command command = router.getCommand(commandType);
            if (command == null) {
                LOG.error("Command not found: {}", commandName);
                request.setAttribute(ERROR_ATTRIBUTE, "Command not found: " + commandName);
                request.getRequestDispatcher(ERROR_PAGE).forward(request, response);
                return;
            }

            String resultPage = command.execute(request, response);
            LOG.debug("Command '{}' returned: {}", commandName, resultPage);

            if (resultPage == null) {
                LOG.debug("Command '{}' returned null, nothing to forward", commandName);
                return;
            }

            if (resultPage.startsWith(REDIRECT_PREFIX)) {
                String redirectUrl = resultPage.substring(REDIRECT_PREFIX.length());
                LOG.debug("Redirecting to: {}", redirectUrl);
                response.sendRedirect(redirectUrl);
            } else {
                LOG.debug("Forwarding to: {}", resultPage);
                request.getRequestDispatcher(resultPage).forward(request, response);
            }
        } catch (Exception e) {
            LOG.error("Error processing command: {}", commandName, e);
            request.setAttribute(ERROR_ATTRIBUTE, "Internal server error: " + e.getMessage());
            request.getRequestDispatcher(ERROR_PAGE).forward(request, response);
        }
    }

    @Override
    public void destroy() {
        LOG.info("FrontController destroyed");
        super.destroy();
    }
}