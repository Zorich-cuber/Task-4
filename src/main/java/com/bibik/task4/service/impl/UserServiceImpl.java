package com.bibik.task4.service.impl;

import com.bibik.task4.dao.UserDao;
import com.bibik.task4.dao.impl.UserDaoImpl;
import com.bibik.task4.entity.User;
import com.bibik.task4.exception.DaoException;
import com.bibik.task4.exception.ServiceException;
import com.bibik.task4.exception.ValidationException;
import com.bibik.task4.service.UserService;
import com.bibik.task4.util.PasswordHasher;
import com.bibik.task4.validator.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class UserServiceImpl implements UserService {

    private static final Logger LOG = LogManager.getLogger(UserServiceImpl.class);

    private final UserDao userDao;

    public UserServiceImpl() {
        this.userDao = new UserDaoImpl();
        LOG.info("UserServiceImpl initialized");
    }

    @Override
    public User authenticate(String login, String password) throws ServiceException {
        LOG.debug("Authenticating user: {}", login);

        if (!Validator.isValidLogin(login)) {
            LOG.warn("Invalid login format: {}", login);
            throw new ServiceException("Invalid login format");
        }

        try {
            User user = userDao.findByLogin(login)
                    .orElseThrow(() -> {
                        LOG.warn("User not found: {}", login);
                        return new ServiceException("User not found: " + login);
                    });

            if (!PasswordHasher.verify(password, user.getPassword())) {
                LOG.warn("Invalid password for user: {}", login);
                throw new ServiceException("Invalid password");
            }

            LOG.info("User authenticated successfully: {}", login);
            return user;
        } catch (DaoException e) {
            LOG.error("Database error while authenticating user: {}", login, e);
            throw new ServiceException("Database error during authentication", e);
        }
    }

    @Override
    public User register(String login, String password, String email)
            throws ValidationException, ServiceException {
        LOG.info("Registering new user: {}", login);

        if (!Validator.isValidLogin(login)) {
            LOG.warn("Invalid login during registration: {}", login);
            throw new ValidationException("Invalid login format", "login");
        }

        if (!Validator.isValidPassword(password)) {
            LOG.warn("Invalid password during registration for user: {}", login);
            throw new ValidationException("Invalid password format", "password");
        }

        if (!Validator.isValidEmail(email)) {
            LOG.warn("Invalid email during registration: {}", email);
            throw new ValidationException("Invalid email format", "email");
        }

        try {
            User user = new User();
            user.setLogin(login);
            user.setPassword(PasswordHasher.hash(password));
            user.setEmail(email);
            user.setRole("USER");
            user.setLocale("en");

            userDao.create(user);
            LOG.info("User registered successfully: {}", login);
            return user;
        } catch (DaoException e) {
            LOG.error("Database error while registering user: {}", login, e);
            throw new ServiceException("Database error during registration", e);
        }
    }

    @Override
    public User getById(int id) throws ServiceException {
        LOG.debug("Getting user by id: {}", id);
        try {
            return userDao.findAll(0, 1).stream()
                    .filter(u -> u.getId() == id)
                    .findFirst()
                    .orElse(null);
        } catch (DaoException e) {
            LOG.error("Database error while getting user by id: {}", id, e);
            throw new ServiceException("Database error while getting user by id", e);
        }
    }

    @Override
    public List<User> getAll(int page, int size) throws ServiceException {
        LOG.debug("Getting all users, page: {}, size: {}", page, size);
        try {
            return userDao.findAll(page * size, size);
        } catch (DaoException e) {
            LOG.error("Database error while getting all users", e);
            throw new ServiceException("Database error while getting all users", e);
        }
    }
}