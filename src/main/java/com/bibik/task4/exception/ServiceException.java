package com.bibik.task4.exception;

public class ServiceException extends Exception {


    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}