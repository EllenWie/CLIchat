package com.db.chat.server.history;

import java.io.IOException;

public class HistoryControllerException extends Exception {
    public HistoryControllerException() {
        super();
    }

    public HistoryControllerException(String message) {
        super(message);
    }

    public HistoryControllerException(String message, Throwable cause) {
        super(message, cause);
    }

    public HistoryControllerException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    @Override
    public synchronized Throwable getCause() {
        return super.getCause();
    }
}
