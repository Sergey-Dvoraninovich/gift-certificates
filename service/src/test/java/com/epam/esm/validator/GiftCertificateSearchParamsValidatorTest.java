package com.epam.esm.validator;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.epam.esm.validator.ValidationError.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GiftCertificateSearchParamsValidatorTest {
    private GiftCertificateSearchParamsValidator searchParamsValidator = new GiftCertificateSearchParamsValidator();

    @ParameterizedTest
    @MethodSource("provideSearchParams")
    void testFindByParams(String tagName, String certificateName, String orderingName,
                          String certificateDescription, String orderingCreateDate,
                          List<ValidationError> expected) {

        List<ValidationError> actual = searchParamsValidator.validate(tagName, certificateName, orderingName,
                                                                      certificateDescription, orderingCreateDate);

        assertEquals(expected, actual);
    }

    private static List<Arguments> provideSearchParams() {
        List<Arguments> testCases = new ArrayList<>();

        testCases.add(Arguments.of("Tag", null, null, null, null, Collections.emptyList()));
        testCases.add(Arguments.of(generateString("Tag", 2), null, null, null, null, Collections.emptyList()));
        testCases.add(Arguments.of(generateString("Tag", 45), null, null, null, null, Collections.emptyList()));
        testCases.add(Arguments.of("Tag!", null, null, null, null, Collections.singletonList(INVALID_SYMBOLS_IN_TAG_NAME)));
        testCases.add(Arguments.of("T", null, null, null, null, Collections.singletonList(TOO_SHORT_TAG_NAME)));
        testCases.add(Arguments.of(generateString("Tag", 46), null, null, null, null, Collections.singletonList(TOO_LONG_TAG_NAME)));

        testCases.add(Arguments.of(null, "Certificate", null, null, null, Collections.emptyList()));
        testCases.add(Arguments.of(null, generateString("Certificate", 2), null, null, null, Collections.emptyList()));
        testCases.add(Arguments.of(null, generateString("Certificate", 45), null, null, null, Collections.emptyList()));
        testCases.add(Arguments.of(null, "Certificate!", null, null, null, Collections.singletonList(INVALID_SYMBOLS_IN_GIFT_CERTIFICATE_NAME)));
        testCases.add(Arguments.of(null, "C", null, null, null, Collections.singletonList(TOO_SHORT_GIFT_CERTIFICATE_NAME)));
        testCases.add(Arguments.of(null, generateString("Certificate", 46), null, null, null, Collections.singletonList(TOO_LONG_GIFT_CERTIFICATE_NAME)));

        testCases.add(Arguments.of(null, null, "Error", null, null, Collections.singletonList(INVALID_NAME_ORDERING_TYPE)));

        testCases.add(Arguments.of(null, null, null, "Description", null, Collections.emptyList()));
        testCases.add(Arguments.of(null, null, null, generateString("Description", 2), null, Collections.emptyList()));
        testCases.add(Arguments.of(null, null, null, generateString("Description", 500), null, Collections.emptyList()));
        testCases.add(Arguments.of(null, null, null, "Description$", null, Collections.singletonList(INVALID_SYMBOLS_IN_GIFT_CERTIFICATE_DESCRIPTION)));
        testCases.add(Arguments.of(null, null, null, "D", null, Collections.singletonList(TOO_SHORT_GIFT_CERTIFICATE_DESCRIPTION)));
        testCases.add(Arguments.of(null, null, null, generateString("Description", 501), null, Collections.singletonList(TOO_LONG_GIFT_CERTIFICATE_DESCRIPTION)));

        testCases.add(Arguments.of(null, null, null, null, "Error", Collections.singletonList(INVALID_CREATE_DATE_ORDERING_TYPE)));

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
