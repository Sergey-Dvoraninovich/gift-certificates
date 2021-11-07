package com.epam.esm.validator;

import com.epam.esm.dto.GiftCertificateDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.time.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.epam.esm.validator.ValidationError.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class GiftCertificateValidatorTest {
    private GiftCertificateValidator giftCertificateValidator = new GiftCertificateValidator();

    @ParameterizedTest
    @MethodSource("provideTagParams")
    void testValidateGiftCertificate(String name, String description, String price,
                          String duration, List<ValidationError> expected) {

        List<ValidationError> actual = giftCertificateValidator.validateParams(name, description, price, duration);

        assertEquals(expected, actual);
    }

    @Test
    void testValidateGiftCertificateRequiredParams() {
        List<ValidationError> expected = Arrays.asList(GIFT_CERTIFICATE_NAME_REQUIRED, GIFT_CERTIFICATE_DESCRIPTION_REQUIRED,
                GIFT_CERTIFICATE_PRICE_REQUIRED, GIFT_CERTIFICATE_DURATION_REQUIRED);
        GiftCertificateDto testCertificate = provideGiftCertificateDto();
        testCertificate.setName(null);
        testCertificate.setDescription(null);
        testCertificate.setPrice(null);
        testCertificate.setDuration(null);

        List<ValidationError> actual = giftCertificateValidator.validateWithRequiredParams(testCertificate);

        assertEquals(expected, actual);
    }

    private static List<Arguments> provideTagParams() {
        List<Arguments> testCases = new ArrayList<>();

        testCases.add(Arguments.of("name", null, null, null, Collections.emptyList()));
        testCases.add(Arguments.of(generateString("name", 2),null, null, null, Collections.emptyList()));
        testCases.add(Arguments.of(generateString("name", 45), null, null, null, Collections.emptyList()));
        testCases.add(Arguments.of("name!", null, null, null, Collections.singletonList(INVALID_SYMBOLS_IN_GIFT_CERTIFICATE_NAME)));
        testCases.add(Arguments.of("name ", null, null, null, Collections.singletonList(INVALID_LEADING_OR_CLOSING_SYMBOLS_IN_GIFT_CERTIFICATE_NAME)));
        testCases.add(Arguments.of("n", null, null, null, Collections.singletonList(TOO_SHORT_GIFT_CERTIFICATE_NAME)));
        testCases.add(Arguments.of(generateString("name", 46), null, null, null, Collections.singletonList(TOO_LONG_GIFT_CERTIFICATE_NAME)));

        testCases.add(Arguments.of(null, "description", null, null, Collections.emptyList()));
        testCases.add(Arguments.of(null, generateString("description", 2), null, null, Collections.emptyList()));
        testCases.add(Arguments.of(null, generateString("description",500), null, null, Collections.emptyList()));
        testCases.add(Arguments.of(null, "description$", null, null, Collections.singletonList(INVALID_SYMBOLS_IN_GIFT_CERTIFICATE_DESCRIPTION)));
        testCases.add(Arguments.of(null, " description", null, null, Collections.singletonList(INVALID_LEADING_OR_CLOSING_SYMBOLS_IN_GIFT_CERTIFICATE_DESCRIPTION)));
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

    private GiftCertificateDto provideGiftCertificateDto() {
        GiftCertificateDto certificate = new GiftCertificateDto();
        certificate.setId(1L);
        certificate.setName("certificate first and second tags");
        certificate.setDescription("certificate with first tag and second tag");
        certificate.setPrice(new BigDecimal("50.00"));
        certificate.setDuration(Duration.ofDays(90));
        certificate.setTagsDto(Collections.emptyList());
        Instant date = Instant.from(ZonedDateTime.of(2000, 1, 1, 11, 11, 11, 222000000, ZoneId.of("Europe/Minsk")));
        certificate.setCreateDate(date);
        certificate.setLastUpdateDate(date);
        return certificate;
    }
}
