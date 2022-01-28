package com.epam.esm.exception;

public class RefreshTokenException extends RuntimeException {
    public enum State {
        TOKEN_EXPIRED,
        INVALID_TOKEN,
    }

    private final State state;

    public RefreshTokenException(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }
}
