package com.epam.esm.validator;

import com.epam.esm.exception.InvalidPaginationException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.epam.esm.validator.ValidationError.TOO_BIG_PAGE_SIZE;
import static com.epam.esm.validator.ValidationError.TOO_SMALL_PAGE_NUMBER;
import static com.epam.esm.validator.ValidationError.TOO_SMALL_PAGE_SIZE;

@Component
public class PaginationValidator {
    private static final int MIN_PAGE_NUMBER = 1;
    private static final int MIN_PAGE_SIZE = 1;
    private static final int MAX_PAGE_SIZE = 100;

    public List<ValidationError> validateParams(Integer pageNumber, Integer pageSize) throws InvalidPaginationException {
        List<ValidationError> paginationErrors = new ArrayList<>();
        if (pageNumber != null) {
            if (pageNumber < MIN_PAGE_NUMBER) {
                paginationErrors.add(TOO_SMALL_PAGE_NUMBER);
            }
        }

        if (pageSize != null) {
            if (pageSize < MIN_PAGE_SIZE) {
                paginationErrors.add(TOO_SMALL_PAGE_SIZE);
            } else if (pageSize > MAX_PAGE_SIZE) {
                paginationErrors.add(TOO_BIG_PAGE_SIZE);
            }
        }

        return paginationErrors;
    }
}
