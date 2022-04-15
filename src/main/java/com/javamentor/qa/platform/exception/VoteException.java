package com.javamentor.qa.platform.exception;

public class VoteException extends RuntimeException {
    private static final long serialVersionUID = -8069721894426741488L;

    public VoteException() {
    }

    public VoteException(String message) {
        super(message);
    }
}
