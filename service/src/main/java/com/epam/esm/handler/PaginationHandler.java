package com.epam.esm.handler;

import com.epam.esm.exception.InvalidEntityException;
import com.epam.esm.exception.InvalidPaginationException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import static com.epam.esm.exception.InvalidPaginationException.*;

@Component
@NoArgsConstructor
public class PaginationHandler {
    private static final int MIN_PAGE_NUMBER = 1;
    private static final int MIN_PAGE_SIZE = 1;
    private static final int MAX_PAGE_SIZE = 100;
    private static final int DEFAULT_PAGE_SIZE = 1;

    private Integer pageNumber = MIN_PAGE_NUMBER;
    private Integer pageSize = DEFAULT_PAGE_SIZE;

    public void handle(Integer pageNumber, Integer pageSize) throws InvalidPaginationException {
        if (pageNumber != null) {
            if (pageNumber < MIN_PAGE_NUMBER) {
                throw new InvalidPaginationException(pageNumber, pageSize, PaginationError.TOO_SMALL_PAGE_NUMBER);
            }
            else {
                this.pageNumber = pageNumber;
            }
        }

        if (pageSize != null) {
            if (pageSize < MIN_PAGE_SIZE) {
                throw new InvalidPaginationException(pageNumber, pageSize, PaginationError.TOO_SMALL_PAGE_SIZE);
            }
            else if (pageSize > MAX_PAGE_SIZE) {
                throw new InvalidPaginationException(pageNumber, pageSize, PaginationError.TOO_BIG_PAGE_SIZE);
            }
            else {
                this.pageSize = pageSize;
            }
        }
    }

    public int getMinPos() {
        return (pageNumber - 1) * pageSize;
    }

    public int getMaxPos() {
        return pageNumber * pageSize;
    }
}
