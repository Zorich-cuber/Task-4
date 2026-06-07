package com.bibik.task4.validator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.regex.Pattern;

public final class InputValidator {

    private static final Logger LOG = LogManager.getLogger(InputValidator.class);


    private static final Pattern LOGIN_PATTERN;
    private static final Pattern EMAIL_PATTERN;
    private static final Pattern NAME_PATTERN;


    private static final int MIN_LOGIN_LENGTH;
    private static final int MAX_LOGIN_LENGTH;
    private static final int MIN_PASSWORD_LENGTH;
    private static final int MAX_PASSWORD_LENGTH;
    private static final int MIN_QUANTITY;
    private static final int MAX_QUANTITY;
    private static final BigDecimal MIN_PRICE;
    private static final BigDecimal MAX_PRICE;

    static {
        LOGIN_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{3,50}$");
        EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
        NAME_PATTERN = Pattern.compile("^[a-zA-Zа-яА-ЯёЁ0-9\\s\\-]{2,200}$");

        MIN_LOGIN_LENGTH = 3;
        MAX_LOGIN_LENGTH = 50;
        MIN_PASSWORD_LENGTH = 6;
        MAX_PASSWORD_LENGTH = 100;
        MIN_QUANTITY = 1;
        MAX_QUANTITY = 1000;
        MIN_PRICE = new BigDecimal("0.01");
        MAX_PRICE = new BigDecimal("999999.99");

        LOG.info("Validator static initialization completed");
    }

    private InputValidator() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static boolean isValidLogin(String login) {
        if (login == null) {
            LOG.debug("Login is null");
            return false;
        }
        boolean valid = LOGIN_PATTERN.matcher(login).matches();
        if (!valid) {
            LOG.warn("Invalid login format: '{}'", login);
        }
        return valid;
    }

    public static boolean isValidPassword(String password) {
        if (password == null) {
            LOG.debug("Password is null");
            return false;
        }
        boolean valid = password.length() >= MIN_PASSWORD_LENGTH && password.length() <= MAX_PASSWORD_LENGTH;
        if (!valid) {
            LOG.warn("Invalid password length: {}", password.length());
        }
        return valid;
    }

    public static boolean isValidEmail(String email) {
        if (email == null) {
            LOG.debug("Email is null");
            return false;
        }
        boolean valid = EMAIL_PATTERN.matcher(email).matches();
        if (!valid) {
            LOG.warn("Invalid email format: '{}'", email);
        }
        return valid;
    }

    public static boolean isValidName(String name) {
        if (name == null) {
            LOG.debug("Name is null");
            return false;
        }
        boolean valid = NAME_PATTERN.matcher(name).matches();
        if (!valid) {
            LOG.warn("Invalid name format: '{}'", name);
        }
        return valid;
    }

    public static boolean isValidQuantity(int quantity) {
        boolean valid = quantity >= MIN_QUANTITY && quantity <= MAX_QUANTITY;
        if (!valid) {
            LOG.warn("Invalid quantity: {}", quantity);
        }
        return valid;
    }

    public static boolean isValidPrice(BigDecimal price) {
        if (price == null) {
            LOG.debug("Price is null");
            return false;
        }
        boolean valid = price.compareTo(MIN_PRICE) >= 0 && price.compareTo(MAX_PRICE) <= 0;
        if (!valid) {
            LOG.warn("Invalid price: {}", price);
        }
        return valid;
    }

    public static boolean isValidId(int id) {
        boolean valid = id > 0;
        if (!valid) {
            LOG.warn("Invalid id: {}", id);
        }
        return valid;
    }

    public static boolean isNotBlank(String value) {
        boolean valid = value != null && !value.trim().isEmpty();
        if (!valid) {
            LOG.debug("Value is blank or null");
        }
        return valid;
    }
}
