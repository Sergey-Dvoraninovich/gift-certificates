package com.epam.esm.validator;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.epam.esm.validator.ValidationError.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaginationValidatorTest {
    private PaginationValidator paginationValidator = new PaginationValidator();

    @ParameterizedTest
    @MethodSource("providePaginationParams")
    void testOrderCreateValidator(String pageNumber, String pageSize, List<ValidationError> expected) {

        List<ValidationError> actual = paginationValidator.validateParams(pageNumber, pageSize);

        assertEquals(expected, actual);
    }

    private static List<Arguments> providePaginationParams() {
        List<Arguments> testCases = new ArrayList<>();

        testCases.add(Arguments.of("10", "10", Collections.emptyList()));
        testCases.add(Arguments.of("0", "10", Collections.singletonList(TOO_SMALL_PAGE_NUMBER)));
        testCases.add(Arguments.of("10", "0", Collections.singletonList(TOO_SMALL_PAGE_SIZE)));
        testCases.add(Arguments.of("10", "100000", Collections.singletonList(TOO_BIG_PAGE_SIZE)));

        return testCases;
    }
}
