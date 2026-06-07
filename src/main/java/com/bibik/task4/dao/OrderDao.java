package com.bibik.task4.dao;

import com.bibik.task4.entity.Order;
import com.bibik.task4.exception.DaoException;

import java.util.List;
import java.util.Optional;

public interface OrderDao {
    Order create(Order order) throws DaoException;
    List<Order> findByUserId(int userId) throws DaoException;
    Optional<Order> findById(int id) throws DaoException;
    boolean cancel(int orderId, int userId) throws DaoException;
    int count() throws DaoException;
}
