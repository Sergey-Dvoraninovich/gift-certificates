package com.epam.esm.exception;

public class EntityAlreadyExistsException extends RuntimeException {
    private final Class<?> causeEntity;

    public EntityAlreadyExistsException() {
        this(null);
    }

    public EntityAlreadyExistsException(Class<?> causeEntity) {
        this.causeEntity = causeEntity;
    }

    public Class<?> getCauseEntity() {
        return causeEntity;
    }
}
