package com.epam.esm.exception;

import com.epam.esm.validator.ValidationError;

import java.util.List;

public class InvalidPaginationException extends RuntimeException {
    private final Integer pageNumber;
    private final Integer pageSize;
    private final List<ValidationError> paginationErrors;

    public InvalidPaginationException(Integer pageNumber, Integer pageSize, List<ValidationError> paginationErrors) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.paginationErrors = paginationErrors;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public List<ValidationError> getPaginationErrors() {
        return paginationErrors;
    }
}
