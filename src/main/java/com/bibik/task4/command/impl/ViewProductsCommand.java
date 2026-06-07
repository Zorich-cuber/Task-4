package com.bibik.task4.command.impl;

import com.bibik.task4.command.Command;
import com.bibik.task4.exception.ServiceException;
import com.bibik.task4.service.ProductService;
import com.bibik.task4.service.impl.ProductServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ViewProductsCommand implements Command {

    private static final Logger LOG = LogManager.getLogger(ViewProductsCommand.class);
    private static final String PRODUCTS_PAGE = "/WEB-INF/jsp/products.jsp";
    private static final String ERROR_PAGE = "/WEB-INF/jsp/error.jsp";
    private static final int PAGE_SIZE = 5;
    private static final String ERROR_ATTRIBUTE = "error";

    private final ProductService productService;

    public ViewProductsCommand() {
        this.productService = new ProductServiceImpl();
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        int page = 0;
        try {
            String pageParam = request.getParameter("page");
            if (pageParam != null) {
                page = Integer.parseInt(pageParam);
            }
            LOG.debug("Viewing products page: {}", page);
        } catch (NumberFormatException e) {
            LOG.warn("Invalid page parameter, using default: 0");
        }

        try {
            request.setAttribute("products", productService.getAll(page, PAGE_SIZE));
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", productService.getTotalPages(PAGE_SIZE));
            LOG.debug("Products page {} rendered", page + 1);
            return PRODUCTS_PAGE;
        } catch (ServiceException e) {
            LOG.error("ServiceException while loading products", e);
            request.setAttribute(ERROR_ATTRIBUTE, "Cannot load products: " + e.getMessage());
            return ERROR_PAGE;
        }
    }
}