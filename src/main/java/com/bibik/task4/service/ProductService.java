package com.bibik.task4.service;

import com.bibik.task4.entity.Product;
import com.bibik.task4.exception.ServiceException;

import java.util.List;

public interface ProductService {
    Product getById(int id) throws ServiceException;
    List<Product> getAll(int page, int size) throws ServiceException;
    int getTotalPages(int size) throws ServiceException;
}
