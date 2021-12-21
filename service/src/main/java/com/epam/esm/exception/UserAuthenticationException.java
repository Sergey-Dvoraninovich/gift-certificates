package com.epam.esm.exception;

public class UserAuthenticationException extends RuntimeException {
    public enum State {
        NO_USER_WITH_SUCH_LOGIN,
        INVALID_LOGIN,
        INVALID_PASSWORD,
    }

    private final UserAuthenticationException.State state;

    public UserAuthenticationException(UserAuthenticationException.State state) {
        this.state = state;
    }

    public UserAuthenticationException.State getState() {
        return state;
    }
}
