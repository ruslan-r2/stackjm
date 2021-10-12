package com.javamentor.qa.platform.exception;

public class TagAlreadyExistsException extends RuntimeException {
    private static final long serialVersionUID = -6711820158011040724L;

    public TagAlreadyExistsException() {
    }

    public TagAlreadyExistsException(String message) {
        super(message);
    }

}
