package com.bibik.task4.command.impl;

import com.bibik.task4.command.Command;
import com.bibik.task4.entity.Product;
import com.bibik.task4.exception.ServiceException;
import com.bibik.task4.service.ProductService;
import com.bibik.task4.service.impl.ProductServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ViewProductCommand implements Command {

    private static final Logger LOG = LogManager.getLogger(ViewProductCommand.class);
    private static final String PRODUCT_PAGE = "/WEB-INF/jsp/product.jsp";
    private static final String ERROR_PAGE = "/WEB-INF/jsp/error.jsp";
    private static final String ERROR_ATTRIBUTE = "error";

    private final ProductService productService;

    public ViewProductCommand() {
        this.productService = new ProductServiceImpl();
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        int id;
        try {
            id = Integer.parseInt(request.getParameter("id"));
            LOG.debug("Viewing product: {}", id);
        } catch (NumberFormatException e) {
            LOG.error("Invalid product id", e);
            request.setAttribute(ERROR_ATTRIBUTE, "Invalid product id");
            return ERROR_PAGE;
        }

        try {
            Product product = productService.getById(id);
            request.setAttribute("product", product);
            return PRODUCT_PAGE;
        } catch (ServiceException e) {
            LOG.error("ServiceException while loading product: {}", id, e);
            request.setAttribute(ERROR_ATTRIBUTE, "Cannot load product: " + e.getMessage());
            return ERROR_PAGE;
        }
    }
}
