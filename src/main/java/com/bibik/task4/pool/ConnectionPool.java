package com.bibik.task4.pool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConnectionPool {

    private static final Logger LOG = LogManager.getLogger(ConnectionPool.class);


    private static final String DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/book_store?useUnicode=true&characterEncoding=UTF-8";
    private static final String USER = "root";
    private static final String PASSWORD = "12345";


    private static final int POOL_SIZE = 10;
    private static final int MAX_CONNECTIONS = POOL_SIZE * 2;

    private final ConcurrentLinkedQueue<Connection> connections;
    private final AtomicInteger createdConnections;

    private static final ConnectionPool INSTANCE = new ConnectionPool();

    private ConnectionPool() {
        this.connections = new ConcurrentLinkedQueue<>();
        this.createdConnections = new AtomicInteger(0);
        try {
            Class.forName(DRIVER_CLASS);
            for (int i = 0; i < POOL_SIZE; i++) {
                connections.offer(createConnection());
                createdConnections.incrementAndGet();
            }
            LOG.info("Connection pool initialized with {} connections", POOL_SIZE);
        } catch (ClassNotFoundException | SQLException e) {
            LOG.error("Failed to initialize connection pool", e);
            throw new RuntimeException(e);
        }
    }

    public static ConnectionPool getInstance() {
        return INSTANCE;
    }

    public Connection getConnection() throws SQLException {
        Connection connection = connections.poll();
        if (connection != null && !connection.isClosed()) {
            return connection;
        }
        if (createdConnections.get() < MAX_CONNECTIONS) {
            createdConnections.incrementAndGet();
            return createConnection();
        }
        throw new SQLException("Connection pool exhausted");
    }

    public void releaseConnection(Connection connection) {
        if (connection != null) {
            connections.offer(connection);
        }
    }

    private Connection createConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public void shutdown() {
        Connection connection;
        while ((connection = connections.poll()) != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOG.error("Error closing connection", e);
            }
        }
        LOG.info("Connection pool shut down");
    }
}