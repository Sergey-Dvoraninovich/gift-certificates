package com.epam.esm.exception;

import com.epam.esm.validator.ValidationError;

import java.util.List;

public class InvalidPaginationException extends RuntimeException {
    private final String pageNumber;
    private final String  pageSize;
    private final List<ValidationError> paginationErrors;

    public InvalidPaginationException(String pageNumber, String pageSize, List<ValidationError> paginationErrors) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.paginationErrors = paginationErrors;
    }

    public String getPageNumber() {
        return pageNumber;
    }

    public String getPageSize() {
        return pageSize;
    }

    public List<ValidationError> getPaginationErrors() {
        return paginationErrors;
    }
}
