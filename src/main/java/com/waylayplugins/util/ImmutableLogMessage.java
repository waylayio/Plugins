package com.waylayplugins.util;

import com.ai.api.LogMessage;

public class ImmutableLogMessage implements LogMessage {

    public static final String WARN = "warn";
    public static final String INFO = "info";
    public static final String ERROR = "error";

    private final String level;
    private final String message;

    public ImmutableLogMessage(String level, String message) {
        this.level = level;
        this.message = message;
    }

    @Override
    public String getLevel() {
        return level;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
