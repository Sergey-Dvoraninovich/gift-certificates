package com.epam.esm.validator;

import com.epam.esm.dto.mapping.MapperConfiguration;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.epam.esm.validator.ValidationError.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TagValidatorTest {
    private TagValidator tagValidator = new TagValidator();

    @ParameterizedTest
    @MethodSource("provideTagParams")
    void testFindByParams(String name, List<ValidationError> expected) {

        List<ValidationError> actual = tagValidator.validate(name);

        assertEquals(expected, actual);
    }

    private static List<Arguments> provideTagParams() {
        List<Arguments> testCases = new ArrayList<>();

        testCases.add(Arguments.of("name", Collections.emptyList()));
        testCases.add(Arguments.of(generateString("name", 3), Collections.emptyList()));
        testCases.add(Arguments.of(generateString("name", 45), Collections.emptyList()));
        testCases.add(Arguments.of("name!", Collections.singletonList(INVALID_SYMBOLS_IN_NAME)));
        testCases.add(Arguments.of(" name ", Collections.singletonList(INVALID_LEADING_OR_CLOSING_SYMBOLS_IN_NAME)));
        testCases.add(Arguments.of("n", Collections.singletonList(TOO_SHORT_NAME)));
        testCases.add(Arguments.of(generateString("name", 46), Collections.singletonList(TOO_LONG_NAME)));

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
