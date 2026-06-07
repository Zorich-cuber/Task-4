package com.bibik.task4.service.impl;

import com.bibik.task4.dao.OrderDao;
import com.bibik.task4.dao.ProductDao;
import com.bibik.task4.dao.impl.OrderDaoImpl;
import com.bibik.task4.dao.impl.ProductDaoImpl;
import com.bibik.task4.entity.Order;
import com.bibik.task4.entity.Product;
import com.bibik.task4.exception.DaoException;
import com.bibik.task4.exception.ServiceException;
import com.bibik.task4.service.OrderService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderServiceImpl implements OrderService {

    private static final Logger LOG = LogManager.getLogger(OrderServiceImpl.class);

    private final OrderDao orderDao;
    private final ProductDao productDao;

    public OrderServiceImpl() {
        this.orderDao = new OrderDaoImpl();
        this.productDao = new ProductDaoImpl();
        LOG.info("OrderServiceImpl initialized");
    }

    @Override
    public Order create(int userId, int productId, int quantity) throws ServiceException {
        LOG.info("Creating order: userId={}, productId={}, quantity={}", userId, productId, quantity);

        try {
            Product product = productDao.findById(productId)
                    .orElseThrow(() -> {
                        LOG.error("Product not found: {}", productId);
                        return new ServiceException("Product not found: " + productId);
                    });

            if (product.getQuantity() < quantity) {
                LOG.warn("Insufficient stock: productId={}, available={}, requested={}",
                        productId, product.getQuantity(), quantity);
                throw new ServiceException("Insufficient stock for product: " + productId);
            }

            Order order = new Order();
            order.setUserId(userId);
            order.setProductId(productId);
            order.setQuantity(quantity);
            order.setTotalAmount(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
            order.setStatus("NEW");
            order.setCreatedAt(LocalDateTime.now());

            orderDao.create(order);
            LOG.info("Order created successfully: orderId={}", order.getId());
            return order;
        } catch (DaoException e) {
            LOG.error("Database error while creating order", e);
            throw new ServiceException("Database error while creating order", e);
        }
    }

    @Override
    public List<Order> getByUserId(int userId) throws ServiceException {
        LOG.debug("Getting orders for user: {}", userId);
        try {
            return orderDao.findByUserId(userId);
        } catch (DaoException e) {
            LOG.error("Database error while getting orders for user: {}", userId, e);
            throw new ServiceException("Database error while getting orders", e);
        }
    }

    @Override
    public boolean cancel(int orderId, int userId) throws ServiceException {
        LOG.info("Cancelling order: orderId={}, userId={}", orderId, userId);
        try {
            boolean cancelled = orderDao.cancel(orderId, userId);
            if (cancelled) {
                LOG.info("Order cancelled successfully: orderId={}", orderId);
            } else {
                LOG.warn("Order cancellation failed: orderId={}", orderId);
            }
            return cancelled;
        } catch (DaoException e) {
            LOG.error("Database error while cancelling order: orderId={}", orderId, e);
            throw new ServiceException("Database error while cancelling order", e);
        }
    }
}