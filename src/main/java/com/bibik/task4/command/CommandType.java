package com.bibik.task4.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public enum CommandType {

    LOGIN("login"),
    LOGOUT("logout"),
    REGISTRATION("registration"),
    VIEW_PRODUCTS("viewProducts"),
    VIEW_PRODUCT("viewProduct"),
    CREATE_ORDER("createOrder"),
    VIEW_ORDERS("viewOrders"),
    CANCEL_ORDER("cancelOrder"),
    CHANGE_LOCALE("changeLocale");

    private static final Logger LOG = LogManager.getLogger(CommandType.class);

    private final String name;

    CommandType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static CommandType fromString(String name) {
        if (name == null || name.trim().isEmpty()) {
            LOG.warn("Command name is null/empty, using default: VIEW_PRODUCTS");
            return VIEW_PRODUCTS;
        }
        for (CommandType type : CommandType.values()) {
            if (type.name.equalsIgnoreCase(name.trim())) {
                LOG.debug("Found command type: {}", type);
                return type;
            }
        }
        LOG.warn("Unknown command: '{}', using default: VIEW_PRODUCTS", name);
        return VIEW_PRODUCTS;
    }
}
