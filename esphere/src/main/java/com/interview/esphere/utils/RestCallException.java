package com.interview.esphere.utils;

public class RestCallException extends Exception {

    public RestCallException() {
        super();
    }

    public RestCallException(String message) {
        super(message);
    }

    public RestCallException(String message, Throwable cause) {
        super(message, cause);
    }

    public RestCallException(Throwable cause) {
        super(cause);
    }

    protected RestCallException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
