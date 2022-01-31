package com.epam.esm.validator;

import com.epam.esm.exception.InvalidPaginationException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.epam.esm.validator.ValidationError.*;

@Component
public class PaginationValidator {
    private static final int MIN_PAGE_NUMBER = 1;
    private static final int MIN_PAGE_SIZE = 1;
    private static final int MAX_PAGE_SIZE = 100;

    public List<ValidationError> validateParams(String pageNumberLine, String pageSizeLine) throws InvalidPaginationException {
        List<ValidationError> paginationErrors = new ArrayList<>();
        if (pageNumberLine != null) {
            try {
                int pageNumber = Integer.parseInt(pageNumberLine);
                if (pageNumber < MIN_PAGE_NUMBER) {
                    paginationErrors.add(TOO_SMALL_PAGE_NUMBER);
                }
            } catch (NumberFormatException e) {
                paginationErrors.add(INVALID_PAGE_NUMBER);
            }
        }

        if (pageSizeLine != null) {
            try {
                int pageSize = Integer.parseInt(pageSizeLine);
                if (pageSize < MIN_PAGE_SIZE) {
                    paginationErrors.add(TOO_SMALL_PAGE_SIZE);
                } else if (pageSize > MAX_PAGE_SIZE) {
                    paginationErrors.add(TOO_BIG_PAGE_SIZE);
                }
            } catch (NumberFormatException e) {
                paginationErrors.add(INVALID_PAGE_SIZE);
            }
        }

        return paginationErrors;
    }
}
