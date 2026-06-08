package com.bibik.task4.dao.impl;

import com.bibik.task4.dao.UserDao;
import com.bibik.task4.entity.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.bibik.task4.exception.DaoException;
import com.bibik.task4.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserDaoImpl implements UserDao {

    private static final Logger LOG = LogManager.getLogger(UserDaoImpl.class);

    private static final String INSERT =
            "INSERT INTO users (login, password, email, role, locale) VALUES (?, ?, ?, ?, ?)";
    private static final String FIND_BY_LOGIN =
            "SELECT id, login, password, email, role, locale FROM users WHERE login = ?";
    private static final String FIND_BY_ID =
            "SELECT id, login, password, email, role, locale FROM users WHERE id = ?";
    private static final String FIND_ALL =
            "SELECT id, login, password, email, role, locale FROM users LIMIT ? OFFSET ?";

    @Override
    public User create(User user) throws DaoException {
        LOG.info("Creating user: {}", user.getLogin());
        Connection conn = null;
        try {
            conn = ConnectionPool.getInstance().getConnection();
            try (PreparedStatement ps = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, user.getLogin());
                ps.setString(2, user.getPassword());
                ps.setString(3, user.getEmail());
                ps.setString(4, user.getRole());
                ps.setString(5, user.getLocale());
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        user.setId(rs.getInt(1));
                        LOG.info("User created: id={}", user.getId());
                    }
                }
                return user;
            }
        } catch (SQLException e) {
            LOG.error("Error creating user: {}", user.getLogin(), e);
            throw new DaoException("Error creating user: " + user.getLogin(), e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);  
        }
    }

    @Override
    public Optional<User> findByLogin(String login) throws DaoException {
        LOG.debug("Finding user by login: {}", login);
        Connection conn = null;
        try {
            conn = ConnectionPool.getInstance().getConnection();
            try (PreparedStatement ps = conn.prepareStatement(FIND_BY_LOGIN)) {
                ps.setString(1, login);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        LOG.debug("User found: {}", login);
                        return Optional.of(mapUser(rs));
                    }
                }
            }
        } catch (SQLException e) {
            LOG.error("Error finding user by login: {}", login, e);
            throw new DaoException("Error finding user by login: " + login, e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn); 
        }
        LOG.debug("User not found: {}", login);
        return Optional.empty();
    }

    @Override
    public Optional<User> findById(int id) throws DaoException {
        LOG.debug("Finding user by id: {}", id);
        Connection conn = null;
        try {
            conn = ConnectionPool.getInstance().getConnection();
            try (PreparedStatement ps = conn.prepareStatement(FIND_BY_ID)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        LOG.debug("User found: {}", id);
                        return Optional.of(mapUser(rs));
                    }
                }
            }
        } catch (SQLException e) {
            LOG.error("Error finding user by id: {}", id, e);
            throw new DaoException("Error finding user by id: " + id, e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn); 
        }
        LOG.debug("User not found: {}", id);
        return Optional.empty();
    }

    @Override
    public List<User> findAll(int offset, int limit) throws DaoException {
        LOG.debug("Finding all users, offset={}, limit={}", offset, limit);
        List<User> users = new ArrayList<>();
        Connection conn = null;
        try {
            conn = ConnectionPool.getInstance().getConnection();
            try (PreparedStatement ps = conn.prepareStatement(FIND_ALL)) {
                ps.setInt(1, limit);
                ps.setInt(2, offset);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        users.add(mapUser(rs));
                    }
                    LOG.debug("Found {} users", users.size());
                }
            }
            return users;
        } catch (SQLException e) {
            LOG.error("Error finding all users", e);
            throw new DaoException("Error finding all users", e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);  
        }
    }

    private User mapUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setLogin(rs.getString("login"));
        user.setPassword(rs.getString("password"));
        user.setEmail(rs.getString("email"));
        user.setRole(rs.getString("role"));
        user.setLocale(rs.getString("locale"));
        return user;
    }
}
