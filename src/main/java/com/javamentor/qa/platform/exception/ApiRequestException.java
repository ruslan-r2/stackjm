package com.javamentor.qa.platform.exception;

public class ApiRequestException extends RuntimeException {

    private static final long serialVersionUID = -2061683015101611282L;

    public ApiRequestException(String message) {
        super(message);
    }
}
