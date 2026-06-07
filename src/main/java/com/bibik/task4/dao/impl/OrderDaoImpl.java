package com.bibik.task4.dao.impl;

import com.bibik.task4.dao.OrderDao;
import com.bibik.task4.entity.Order;
import com.bibik.task4.exception.DaoException;
import com.bibik.task4.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderDaoImpl implements OrderDao {

    private static final Logger LOG = LogManager.getLogger(OrderDaoImpl.class);

    private static final String INSERT =
            "INSERT INTO orders (user_id, product_id, quantity, total_amount, status) VALUES (?, ?, ?, ?, 'NEW')";
    private static final String FIND_BY_USER =
            "SELECT * FROM orders WHERE user_id = ? ORDER BY created_at DESC";
    private static final String FIND_BY_ID = "SELECT * FROM orders WHERE id = ?";
    private static final String CANCEL =
            "UPDATE orders SET status = 'CANCELLED' WHERE id = ? AND user_id = ? AND status = 'NEW'";
    private static final String COUNT = "SELECT COUNT(*) FROM orders";

    @Override
    public Order create(Order order) throws DaoException {
        LOG.info("Creating order: userId={}, productId={}", order.getUserId(), order.getProductId());
        try (Connection conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, order.getUserId());
            ps.setInt(2, order.getProductId());
            ps.setInt(3, order.getQuantity());
            ps.setBigDecimal(4, order.getTotalAmount());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    order.setId(rs.getInt(1));
                    LOG.info("Order created: id={}", order.getId());
                }
            }
            return order;
        } catch (SQLException e) {
            LOG.error("Error creating order for user: {}", order.getUserId(), e);
            throw new DaoException("Error creating order for user: " + order.getUserId(), e);
        }
    }

    @Override
    public List<Order> findByUserId(int userId) throws DaoException {
        LOG.debug("Finding orders by user id: {}", userId);
        List<Order> orders = new ArrayList<>();
        try (Connection conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_BY_USER)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapOrder(rs));
                }
                LOG.debug("Found {} orders for user: {}", orders.size(), userId);
            }
            return orders;
        } catch (SQLException e) {
            LOG.error("Error finding orders by user id: {}", userId, e);
            throw new DaoException("Error finding orders by user id: " + userId, e);
        }
    }

    @Override
    public Optional<Order> findById(int id) throws DaoException {
        LOG.debug("Finding order by id: {}", id);
        try (Connection conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    LOG.debug("Order found: {}", id);
                    return Optional.of(mapOrder(rs));
                }
            }
        } catch (SQLException e) {
            LOG.error("Error finding order by id: {}", id, e);
            throw new DaoException("Error finding order by id: " + id, e);
        }
        LOG.debug("Order not found: {}", id);
        return Optional.empty();
    }

    @Override
    public boolean cancel(int orderId, int userId) throws DaoException {
        LOG.info("Cancelling order: orderId={}, userId={}", orderId, userId);
        try (Connection conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(CANCEL)) {
            ps.setInt(1, orderId);
            ps.setInt(2, userId);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                LOG.info("Order cancelled: orderId={}", orderId);
                return true;
            }
            LOG.warn("Order not cancelled: orderId={}", orderId);
            return false;
        } catch (SQLException e) {
            LOG.error("Error cancelling order: orderId={}, userId={}", orderId, userId, e);
            throw new DaoException("Error cancelling order: " + orderId, e);
        }
    }

    @Override
    public int count() throws DaoException {
        LOG.debug("Counting orders");
        try (Connection conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(COUNT);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                int count = rs.getInt(1);
                LOG.debug("Total orders count: {}", count);
                return count;
            }
            return 0;
        } catch (SQLException e) {
            LOG.error("Error counting orders", e);
            throw new DaoException("Error counting orders", e);
        }
    }

    private Order mapOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getInt("id"));
        order.setUserId(rs.getInt("user_id"));
        order.setProductId(rs.getInt("product_id"));
        order.setQuantity(rs.getInt("quantity"));
        order.setTotalAmount(rs.getBigDecimal("total_amount"));
        order.setStatus(rs.getString("status"));
        Timestamp createdAt = rs.getTimestamp("created_at");
        order.setCreatedAt(createdAt != null ? createdAt.toLocalDateTime() : null);
        return order;
    }
}