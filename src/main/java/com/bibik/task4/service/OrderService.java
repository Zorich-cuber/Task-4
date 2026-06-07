package com.bibik.task4.service;

import com.bibik.task4.entity.Order;
import com.bibik.task4.exception.ServiceException;

import java.util.List;

public interface OrderService {
    Order create(int userId, int productId, int quantity) throws ServiceException;
    List<Order> getByUserId(int userId) throws ServiceException;
    boolean cancel(int orderId, int userId) throws ServiceException;
}