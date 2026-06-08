package com.bibik.task4.dao.impl;

import com.bibik.task4.dao.ProductDao;
import com.bibik.task4.entity.Product;
import com.bibik.task4.exception.DaoException;
import com.bibik.task4.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductDaoImpl implements ProductDao {

    private static final Logger LOG = LogManager.getLogger(ProductDaoImpl.class);

    private static final String INSERT =
            "INSERT INTO products (name, description, price, quantity) VALUES (?, ?, ?, ?)";
    private static final String FIND_ALL =
            "SELECT id, name, description, price, quantity FROM products LIMIT ? OFFSET ?";
    private static final String FIND_BY_ID =
            "SELECT id, name, description, price, quantity FROM products WHERE id = ?";
    private static final String COUNT = "SELECT COUNT(*) FROM products";

    @Override
    public Product create(Product product) throws DaoException {
        LOG.info("Creating product: {}", product.getName());
        Connection conn = null;
        try {
            conn = ConnectionPool.getInstance().getConnection();
            try (PreparedStatement ps = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, product.getName());
                ps.setString(2, product.getDescription());
                ps.setBigDecimal(3, product.getPrice());
                ps.setInt(4, product.getQuantity());
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        product.setId(rs.getInt(1));
                        LOG.info("Product created: id={}", product.getId());
                    }
                }
                return product;
            }
        } catch (SQLException e) {
            LOG.error("Error creating product: {}", product.getName(), e);
            throw new DaoException("Error creating product: " + product.getName(), e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);  
        }
    }

    @Override
    public List<Product> findAll(int offset, int limit) throws DaoException {
        LOG.debug("Finding all products, offset={}, limit={}", offset, limit);
        List<Product> products = new ArrayList<>();
        Connection conn = null;
        try {
            conn = ConnectionPool.getInstance().getConnection();
            try (PreparedStatement ps = conn.prepareStatement(FIND_ALL)) {
                ps.setInt(1, limit);
                ps.setInt(2, offset);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        products.add(mapProduct(rs));
                    }
                    LOG.debug("Found {} products", products.size());
                }
            }
            return products;
        } catch (SQLException e) {
            LOG.error("Error finding all products", e);
            throw new DaoException("Error finding all products", e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);  
        }
    }

    @Override
    public Optional<Product> findById(int id) throws DaoException {
        LOG.debug("Finding product by id: {}", id);
        Connection conn = null;
        try {
            conn = ConnectionPool.getInstance().getConnection();
            try (PreparedStatement ps = conn.prepareStatement(FIND_BY_ID)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        LOG.debug("Product found: {}", id);
                        return Optional.of(mapProduct(rs));
                    }
                }
            }
        } catch (SQLException e) {
            LOG.error("Error finding product by id: {}", id, e);
            throw new DaoException("Error finding product by id: " + id, e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);  
        }
        LOG.debug("Product not found: {}", id);
        return Optional.empty();
    }

    @Override
    public int count() throws DaoException {
        LOG.debug("Counting products");
        Connection conn = null;
        try {
            conn = ConnectionPool.getInstance().getConnection();
            try (PreparedStatement ps = conn.prepareStatement(COUNT);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    LOG.debug("Total products count: {}", count);
                    return count;
                }
                return 0;
            }
        } catch (SQLException e) {
            LOG.error("Error counting products", e);
            throw new DaoException("Error counting products", e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn); 
        }
    }

    private Product mapProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(rs.getInt("id"));
        product.setName(rs.getString("name"));
        product.setDescription(rs.getString("description"));
        product.setPrice(rs.getBigDecimal("price"));
        product.setQuantity(rs.getInt("quantity"));
        return product;
    }
}
