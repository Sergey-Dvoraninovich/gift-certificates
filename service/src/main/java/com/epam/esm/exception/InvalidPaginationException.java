package com.epam.esm.exception;

public class InvalidPaginationException extends RuntimeException {
    private final Integer pageNumber;
    private final Integer pageSize;
    private final PaginationError paginationError;

    public InvalidPaginationException(Integer pageNumber, Integer pageSize, PaginationError paginationError) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.paginationError = paginationError;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public PaginationError getPaginationError() {
        return paginationError;
    }

    public enum PaginationError {
        TOO_SMALL_PAGE_NUMBER,

        TOO_SMALL_PAGE_SIZE,
        TOO_BIG_PAGE_SIZE,
    }
}
