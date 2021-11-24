package com.epam.esm.validator;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.epam.esm.repository.OrderingType.ASC;
import static com.epam.esm.repository.OrderingType.DESC;
import static com.epam.esm.validator.ValidationError.INVALID_ORDER_ORDERING_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderSearchParamsValidatorTest {
    private OrderSearchParamsValidator orderSearchParamsValidator = new OrderSearchParamsValidator();

    @ParameterizedTest
    @MethodSource("provideOrderSearchParams")
    void testOrderSearchValidator(String sortOrder, List<ValidationError> expected) {

        List<ValidationError> actual = orderSearchParamsValidator.validate(sortOrder);

        assertEquals(expected, actual);
    }

    private static List<Arguments> provideOrderSearchParams() {
        List<Arguments> testCases = new ArrayList<>();

        testCases.add(Arguments.of(ASC.name(), Collections.emptyList()));
        testCases.add(Arguments.of(DESC.name(), Collections.emptyList()));
        testCases.add(Arguments.of("ASC!", Collections.singletonList(INVALID_ORDER_ORDERING_TYPE)));

        return testCases;
    }
}
