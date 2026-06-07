package com.bibik.task4.service.impl;

import com.bibik.task4.dao.ProductDao;
import com.bibik.task4.dao.impl.ProductDaoImpl;
import com.bibik.task4.entity.Product;
import com.bibik.task4.exception.DaoException;
import com.bibik.task4.exception.ServiceException;
import com.bibik.task4.service.ProductService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class ProductServiceImpl implements ProductService {

    private static final Logger LOG = LogManager.getLogger(ProductServiceImpl.class);

    private final ProductDao productDao;

    public ProductServiceImpl() {
        this.productDao = new ProductDaoImpl();
        LOG.info("ProductServiceImpl initialized");
    }

    @Override
    public Product getById(int id) throws ServiceException {
        LOG.debug("Getting product by id: {}", id);
        try {
            return productDao.findById(id)
                    .orElseThrow(() -> {
                        LOG.warn("Product not found: {}", id);
                        return new ServiceException("Product not found: " + id);
                    });
        } catch (DaoException e) {
            LOG.error("Database error while getting product by id: {}", id, e);
            throw new ServiceException("Database error while getting product by id", e);
        }
    }

    @Override
    public List<Product> getAll(int page, int size) throws ServiceException {
        LOG.debug("Getting all products, page: {}, size: {}", page, size);
        try {
            return productDao.findAll(page * size, size);
        } catch (DaoException e) {
            LOG.error("Database error while getting all products", e);
            throw new ServiceException("Database error while getting all products", e);
        }
    }

    @Override
    public int getTotalPages(int size) throws ServiceException {
        LOG.debug("Getting total pages for size: {}", size);
        try {
            int total = productDao.count();
            int totalPages = (int) Math.ceil((double) total / size);
            LOG.debug("Total pages: {} for size: {}", totalPages, size);
            return totalPages;
        } catch (DaoException e) {
            LOG.error("Database error while getting total pages", e);
            throw new ServiceException("Database error while getting total pages", e);
        }
    }
}
