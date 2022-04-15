package com.javamentor.qa.platform.exception;

public class TagNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 3093952821622304752L;

    public TagNotFoundException() {
    }

    public TagNotFoundException(String message) {
        super(message);
    }

}
