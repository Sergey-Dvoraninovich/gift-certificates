package com.epam.esm.validator;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

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
                          String duration, List<ValidationError> expected) {

        List<ValidationError> actual = giftCertificateValidator.validate(name, description, price, duration);

        assertEquals(expected, actual);
    }

    private static List<Arguments> provideTagParams() {
        List<Arguments> testCases = new ArrayList<>();

        testCases.add(Arguments.of("name", null, null, null, Collections.emptyList()));
        testCases.add(Arguments.of(generateString("name", 3),null, null, null, Collections.emptyList()));
        testCases.add(Arguments.of(generateString("name", 45), null, null, null, Collections.emptyList()));
        testCases.add(Arguments.of("name!", null, null, null, Collections.singletonList(INVALID_SYMBOLS_IN_NAME)));
        testCases.add(Arguments.of("name ", null, null, null, Collections.singletonList(INVALID_LEADING_OR_CLOSING_SYMBOLS_IN_NAME)));
        testCases.add(Arguments.of("n", null, null, null, Collections.singletonList(TOO_SHORT_NAME)));
        testCases.add(Arguments.of(generateString("name", 46), null, null, null, Collections.singletonList(TOO_LONG_NAME)));

        testCases.add(Arguments.of(null, "description", null, null, Collections.emptyList()));
        testCases.add(Arguments.of(null, generateString("description", 3), null, null, Collections.emptyList()));
        testCases.add(Arguments.of(null, generateString("description",500), null, null, Collections.emptyList()));
        testCases.add(Arguments.of(null, "description$", null, null, Collections.singletonList(INVALID_SYMBOLS_IN_GIFT_CERTIFICATE_DESCRIPTION)));
        testCases.add(Arguments.of(null, " description", null, null, Collections.singletonList(INVALID_LEADING_OR_CLOSING_SYMBOLS_IN_DESCRIPTION)));
        testCases.add(Arguments.of(null, "d", null, null, Collections.singletonList(TOO_SHORT_GIFT_CERTIFICATE_DESCRIPTION)));
        testCases.add(Arguments.of(null, generateString("description", 501), null, null, Collections.singletonList(TOO_LONG_GIFT_CERTIFICATE_DESCRIPTION)));

        testCases.add(Arguments.of(null, null, "100", null, Collections.emptyList()));
        testCases.add(Arguments.of(null, null, "100.00", null, Collections.emptyList()));
        testCases.add(Arguments.of(null, null, "9.99", null, Collections.emptyList()));
        testCases.add(Arguments.of(null, null, "10000", null, Collections.emptyList()));
        testCases.add(Arguments.of(null, null, "10+", null, Collections.singletonList(INVALID_GIFT_CERTIFICATE_PRICE_FORMAT)));
        testCases.add(Arguments.of(null, null, "4.99", null, Collections.singletonList(TOO_SMALL_GIFT_CERTIFICATE_PRICE)));
        testCases.add(Arguments.of(null, null, "99999", null, Collections.singletonList(TOO_BIG_GIFT_CERTIFICATE_PRICE)));

        testCases.add(Arguments.of(null, null, null, "90", Collections.emptyList()));
        testCases.add(Arguments.of(null, null, null, "28", Collections.emptyList()));
        testCases.add(Arguments.of(null, null, null, "1096", Collections.emptyList()));
        testCases.add(Arguments.of(null, null, null, "10.5", Collections.singletonList(INVALID_SYMBOLS_IN_GIFT_CERTIFICATE_DURATION)));
        testCases.add(Arguments.of(null, null, null, "10!", Collections.singletonList(INVALID_SYMBOLS_IN_GIFT_CERTIFICATE_DURATION)));
        testCases.add(Arguments.of(null, null, null, "1", Collections.singletonList(TOO_SHORT_GIFT_CERTIFICATE_DURATION)));
        testCases.add(Arguments.of(null, null, null, "10000", Collections.singletonList(TOO_LONG_GIFT_CERTIFICATE_DURATION)));

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
