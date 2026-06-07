package com.bibik.task4.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;


@WebFilter(urlPatterns = "/*")
public class XssFilter implements Filter {

    private static final Logger LOG = LogManager.getLogger(XssFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOG.info("XssFilter initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        LOG.debug("XssFilter processing request: {} {}", httpRequest.getMethod(), httpRequest.getRequestURI());


        XssRequestWrapper wrappedRequest = new XssRequestWrapper(httpRequest);
        chain.doFilter(wrappedRequest, response);
    }

    @Override
    public void destroy() {
        LOG.info("XssFilter destroyed");
    }
}