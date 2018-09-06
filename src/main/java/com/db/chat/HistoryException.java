package com.db.chat;

import java.io.IOException;

public class HistoryException extends Exception {
    public HistoryException() {
        super();
    }

    public HistoryException(String message) {
        super(message);
    }

    public HistoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public HistoryException(Throwable cause) {
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
