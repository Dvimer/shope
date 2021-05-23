package com.dvimer.buildmarket.exception;

public class BadRequestException extends RuntimeException {
    public static final int DEFAULT_HTTP_STATUS = 400;

    private final Integer httpStatus;
    private final Object[] data;

    public BadRequestException(String errorCode, Object... data) {
        this(DEFAULT_HTTP_STATUS, errorCode, data);
    }

    public BadRequestException(Integer httpStatus, Object... data) {
        this.httpStatus = httpStatus;
        this.data = data;
    }


    public BadRequestException(Throwable throwable) {
        this(throwable.getMessage(), throwable);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = DEFAULT_HTTP_STATUS;
        this.data = new Object[0];
    }
}
