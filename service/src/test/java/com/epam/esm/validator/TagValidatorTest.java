package com.epam.esm.validator;

import com.epam.esm.dto.TagDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.epam.esm.validator.ValidationError.INVALID_LEADING_OR_CLOSING_SYMBOLS_IN_TAG_NAME;
import static com.epam.esm.validator.ValidationError.INVALID_SYMBOLS_IN_TAG_NAME;
import static com.epam.esm.validator.ValidationError.TAG_NAME_REQUIRED;
import static com.epam.esm.validator.ValidationError.TOO_LONG_TAG_NAME;
import static com.epam.esm.validator.ValidationError.TOO_SHORT_TAG_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TagValidatorTest {
    private TagValidator tagValidator = new TagValidator();

    @ParameterizedTest
    @MethodSource("provideTagParams")
    void testTagsValidator(String name, List<ValidationError> expected) {

        List<ValidationError> actual = tagValidator.validateParams(name);

        assertEquals(expected, actual);
    }

    @Test
    void testTagsValidator() {
        List<ValidationError> expected = Arrays.asList(TAG_NAME_REQUIRED);
        TagDto testTag = TagDto.builder().build();
        testTag.setName(null);

        List<ValidationError> actual = tagValidator.validateWithRequiredParams(testTag);

        assertEquals(expected, actual);
    }

    private static List<Arguments> provideTagParams() {
        List<Arguments> testCases = new ArrayList<>();

        testCases.add(Arguments.of("name", Collections.emptyList()));
        testCases.add(Arguments.of(generateString("name", 2), Collections.emptyList()));
        testCases.add(Arguments.of(generateString("name", 45), Collections.emptyList()));
        testCases.add(Arguments.of("name!", Collections.singletonList(INVALID_SYMBOLS_IN_TAG_NAME)));
        testCases.add(Arguments.of(" name ", Collections.singletonList(INVALID_LEADING_OR_CLOSING_SYMBOLS_IN_TAG_NAME)));
        testCases.add(Arguments.of("n", Collections.singletonList(TOO_SHORT_TAG_NAME)));
        testCases.add(Arguments.of(generateString("name", 46), Collections.singletonList(TOO_LONG_TAG_NAME)));

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
