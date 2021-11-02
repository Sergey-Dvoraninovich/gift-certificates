package com.epam.esm.validator;

import com.epam.esm.dto.mapping.MapperConfiguration;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.epam.esm.validator.ValidationError.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class GiftCertificateValidatorTest {
    private GiftCertificateValidator giftCertificateValidator = new GiftCertificateValidator();

    @ParameterizedTest
    @MethodSource("provideTagParams")
    void testFindByParams(String name, String description, String price,
                          String duration, String createTime, String lastUpdateTime,
                          List<ValidationError> expected) {

        List<ValidationError> actual = giftCertificateValidator.validate(name, description, price, duration, createTime, lastUpdateTime);

        assertEquals(expected, actual);
    }

    private static List<Arguments> provideTagParams() {
        List<Arguments> testCases = new ArrayList<>();

        testCases.add(Arguments.of("name", null, null, null, null, null, Collections.emptyList()));
        testCases.add(Arguments.of(generateString("name", 3), null, null, null, null, null, Collections.emptyList()));
        testCases.add(Arguments.of(generateString("name", 45), null, null, null, null, null, Collections.emptyList()));
        testCases.add(Arguments.of("name!", null, null, null, null, null, Collections.singletonList(INVALID_SYMBOLS_IN_NAME)));
        testCases.add(Arguments.of("name ", null, null, null, null, null, Collections.singletonList(INVALID_LEADING_OR_CLOSING_SYMBOLS_IN_NAME)));
        testCases.add(Arguments.of("n", null, null, null, null, null, Collections.singletonList(TOO_SHORT_NAME)));
        testCases.add(Arguments.of(generateString("name", 46), null, null, null, null, null, Collections.singletonList(TOO_LONG_NAME)));

        testCases.add(Arguments.of(null, "description", null, null, null, null, Collections.emptyList()));
        testCases.add(Arguments.of(null, generateString("description", 3), null, null, null, null, Collections.emptyList()));
        testCases.add(Arguments.of(null, generateString("description",500), null, null, null, null, Collections.emptyList()));
        testCases.add(Arguments.of(null, "description$", null, null, null, null, Collections.singletonList(INVALID_SYMBOLS_IN_DESCRIPTION)));
        testCases.add(Arguments.of(null, " description", null, null, null, null, Collections.singletonList(INVALID_LEADING_OR_CLOSING_SYMBOLS_IN_DESCRIPTION)));
        testCases.add(Arguments.of(null, "d", null, null, null, null, Collections.singletonList(TOO_SHORT_DESCRIPTION)));
        testCases.add(Arguments.of(null, generateString("description", 501), null, null, null, null, Collections.singletonList(TOO_LONG_DESCRIPTION)));

        testCases.add(Arguments.of(null, null, "100", null, null, null, Collections.emptyList()));
        testCases.add(Arguments.of(null, null, "100.00", null, null, null, Collections.emptyList()));
        testCases.add(Arguments.of(null, null, "9.99", null, null, null, Collections.emptyList()));
        testCases.add(Arguments.of(null, null, "10000", null, null, null, Collections.emptyList()));
        testCases.add(Arguments.of(null, null, "10+", null, null, null, Collections.singletonList(INVALID_PRICE_FORMAT)));
        testCases.add(Arguments.of(null, null, "4.99", null, null, null, Collections.singletonList(TOO_SMALL_PRICE)));
        testCases.add(Arguments.of(null, null, "99999", null, null, null, Collections.singletonList(TOO_BIG_PRICE)));

        testCases.add(Arguments.of(null, null, null, "90", null, null, Collections.emptyList()));
        testCases.add(Arguments.of(null, null, null, "28", null, null, Collections.emptyList()));
        testCases.add(Arguments.of(null, null, null, "1096", null, null, Collections.emptyList()));
        testCases.add(Arguments.of(null, null, null, "10.5", null, null, Collections.singletonList(INVALID_SYMBOLS_IN_DURATION)));
        testCases.add(Arguments.of(null, null, null, "10!", null, null, Collections.singletonList(INVALID_SYMBOLS_IN_DURATION)));
        testCases.add(Arguments.of(null, null, null, "1", null, null, Collections.singletonList(TOO_SHORT_DURATION)));
        testCases.add(Arguments.of(null, null, null, "10000", null, null, Collections.singletonList(TOO_LONG_DURATION)));

        return testCases;
    }

    private static String generateString(String line, int length){
        StringBuilder result = new StringBuilder(line);
        while (result.length() < length){
            result.append(line);
        }
        return result.substring(0, length);
    }
}
