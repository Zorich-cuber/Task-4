package com.bibik.task4.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class XssUtil {

    private static final Logger LOG = LogManager.getLogger(XssUtil.class);
    private static final String XSS_SANITIZE_REGEX = "[<>\"'/\\\\]";
    private XssUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static String escape(String input) {
        if (input == null) {
            LOG.debug("Input is null, returning null");
            return null;
        }

        String escaped = input
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;")
                .replace("/", "&#x2F;");

        if (!input.equals(escaped)) {
            LOG.debug("XSS escaping applied: original length={}, escaped length={}",
                    input.length(), escaped.length());
        }

        return escaped;
    }

    public static String sanitize(String input) {
        if (input == null) {
            return null;
        }
        return input.replaceAll(XSS_SANITIZE_REGEX, "");
    }
}