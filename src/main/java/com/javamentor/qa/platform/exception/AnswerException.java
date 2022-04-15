package com.javamentor.qa.platform.exception;

public class AnswerException extends RuntimeException {

    private static final long serialVersionUID = 7115737071472618624L;

    public AnswerException() {
    }

    public AnswerException(String message) {
        super(message);
    }
}
