package com.bibik.task4.command;

import com.bibik.task4.command.impl.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.EnumMap;
import java.util.Map;

public class Router {

    private static final Logger LOG = LogManager.getLogger(Router.class);

    private final Map<CommandType, Command> commands;

    public Router() {
        this.commands = new EnumMap<>(CommandType.class);
        initializeCommands();
        LOG.info("Router initialized with {} commands", commands.size());
    }

    private void initializeCommands() {
        commands.put(CommandType.LOGIN, new LoginCommand());
        commands.put(CommandType.LOGOUT, new LogoutCommand());
        commands.put(CommandType.REGISTRATION, new RegistrationCommand());
        commands.put(CommandType.VIEW_PRODUCTS, new ViewProductsCommand());
        commands.put(CommandType.VIEW_PRODUCT, new ViewProductCommand());
        commands.put(CommandType.CREATE_ORDER, new CreateOrderCommand());
        commands.put(CommandType.VIEW_ORDERS, new ViewOrdersCommand());
        commands.put(CommandType.CANCEL_ORDER, new CancelOrderCommand());
        commands.put(CommandType.CHANGE_LOCALE, new ChangeLocaleCommand());

        LOG.debug("All commands registered");
    }


    public Command getCommand(CommandType type) {
        Command command = commands.get(type);
        if (command == null) {
            LOG.error("Command not found for type: {}", type);
        }
        return command;
    }
}
