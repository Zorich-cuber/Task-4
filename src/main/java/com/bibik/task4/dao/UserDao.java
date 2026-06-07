package com.bibik.task4.dao;

import com.bibik.task4.entity.User;
import com.bibik.task4.exception.DaoException;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    Optional<User> findByLogin(String login) throws DaoException;
    User create(User user) throws DaoException;
    List<User> findAll(int offset, int limit) throws DaoException;
    Optional<User> findById(int id) throws DaoException;
}
