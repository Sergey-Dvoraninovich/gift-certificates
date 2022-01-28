package com.epam.esm.exception;

public class AccessException extends RuntimeException {
    public enum State {
        INVALID_ORDER_USER
    }

    private final AccessException.State state;

    public AccessException(AccessException.State state) {
        this.state = state;
    }

    public AccessException.State getState() {
        return state;
    }
}
