package com.epam.esm.exception;

public class EntityNotAvailableException extends RuntimeException {
    private final Long entityId;
    private final Class<?> causeEntity;

    public EntityNotAvailableException(Class<?> causeEntity) {
        this(null, causeEntity);
    }

    public EntityNotAvailableException(Long entityId, Class<?> causeEntity) {
        this.entityId = entityId;
        this.causeEntity = causeEntity;
    }

    public Long getEntityId() {
        return entityId;
    }

    public Class<?> getCauseEntity() {
        return causeEntity;
    }
}
