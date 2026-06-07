package com.bibik.task4.service;

import com.bibik.task4.entity.User;
import com.bibik.task4.exception.ServiceException;
import com.bibik.task4.exception.ValidationException;

import java.util.List;

public interface UserService {
    User authenticate(String login, String password) throws ServiceException;
    User register(String login, String password, String email)
            throws ValidationException, ServiceException;
    User getById(int id) throws ServiceException;
    List<User> getAll(int page, int size) throws ServiceException;
}