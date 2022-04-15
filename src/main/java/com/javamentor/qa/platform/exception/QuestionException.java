package com.javamentor.qa.platform.exception;

public class QuestionException extends RuntimeException {

    private static final long serialVersionUID = 5928912919297163881L;

    public QuestionException() {
    }

    public QuestionException(String message) {
        super(message);
    }
}
