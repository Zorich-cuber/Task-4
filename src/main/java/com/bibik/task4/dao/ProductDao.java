package com.bibik.task4.dao;

import com.bibik.task4.entity.Product;
import com.bibik.task4.exception.DaoException;

import java.util.List;
import java.util.Optional;

public interface ProductDao {
    Optional<Product> findById(int id) throws DaoException;
    List<Product> findAll(int offset, int limit) throws DaoException;
    Product create(Product product) throws DaoException;
    int count() throws DaoException;
}