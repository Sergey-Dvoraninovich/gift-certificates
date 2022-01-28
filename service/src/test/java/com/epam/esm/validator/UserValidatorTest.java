package com.epam.esm.validator;

import com.epam.esm.dto.UserSignUpDto;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.epam.esm.validator.ValidationError.INVALID_USER_EMAIL;
import static com.epam.esm.validator.ValidationError.INVALID_USER_LOGIN;
import static com.epam.esm.validator.ValidationError.INVALID_USER_NAME;
import static com.epam.esm.validator.ValidationError.INVALID_USER_PASSWORD;
import static com.epam.esm.validator.ValidationError.INVALID_USER_SURNAME;
import static com.epam.esm.validator.ValidationError.TOO_LONG_USER_EMAIL;
import static com.epam.esm.validator.ValidationError.TOO_LONG_USER_LOGIN;
import static com.epam.esm.validator.ValidationError.TOO_LONG_USER_NAME;
import static com.epam.esm.validator.ValidationError.TOO_LONG_USER_PASSWORD;
import static com.epam.esm.validator.ValidationError.TOO_LONG_USER_SURNAME;
import static com.epam.esm.validator.ValidationError.TOO_SHORT_USER_EMAIL;
import static com.epam.esm.validator.ValidationError.TOO_SHORT_USER_LOGIN;
import static com.epam.esm.validator.ValidationError.TOO_SHORT_USER_NAME;
import static com.epam.esm.validator.ValidationError.TOO_SHORT_USER_PASSWORD;
import static com.epam.esm.validator.ValidationError.TOO_SHORT_USER_SURNAME;
import static com.epam.esm.validator.ValidationError.USER_EMAIL_REQUIRED;
import static com.epam.esm.validator.ValidationError.USER_LOGIN_REQUIRED;
import static com.epam.esm.validator.ValidationError.USER_NAME_REQUIRED;
import static com.epam.esm.validator.ValidationError.USER_PASSWORD_REQUIRED;
import static com.epam.esm.validator.ValidationError.USER_SURNAME_REQUIRED;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserValidatorTest {
    private UserValidator userValidator = new UserValidator();

    @ParameterizedTest
    @MethodSource("provideRequiredUserParams")
    void testUserValidatorRequiredParams(UserSignUpDto userSignUpDto, List<ValidationError> expected) {

        List<ValidationError> actual = userValidator.validateWithRequiredParams(userSignUpDto);

        assertEquals(expected, actual);
    }

    private static List<Arguments> provideRequiredUserParams() {
        List<Arguments> testCases = new ArrayList<>();
        UserSignUpDto signUpDto = provideSignUpDto();

        testCases.add(Arguments.of(signUpDto, Collections.emptyList()));

        signUpDto = provideSignUpDto();
        signUpDto.setLogin(null);
        testCases.add(Arguments.of(signUpDto, Collections.singletonList(USER_LOGIN_REQUIRED)));

        signUpDto = provideSignUpDto();
        signUpDto.setPassword(null);
        testCases.add(Arguments.of(signUpDto, Collections.singletonList(USER_PASSWORD_REQUIRED)));

        signUpDto = provideSignUpDto();
        signUpDto.setName(null);
        testCases.add(Arguments.of(signUpDto, Collections.singletonList(USER_NAME_REQUIRED)));

        signUpDto = provideSignUpDto();
        signUpDto.setSurname(null);
        testCases.add(Arguments.of(signUpDto, Collections.singletonList(USER_SURNAME_REQUIRED)));

        signUpDto = provideSignUpDto();
        signUpDto.setEmail(null);
        testCases.add(Arguments.of(signUpDto, Collections.singletonList(USER_EMAIL_REQUIRED)));

        signUpDto = provideSignUpDto();
        signUpDto.setLogin(null);
        testCases.add(Arguments.of(signUpDto, Collections.singletonList(USER_LOGIN_REQUIRED)));

        return testCases;
    }

    @ParameterizedTest
    @MethodSource("provideUserParams")
    void testUserValidator(UserSignUpDto userSignUpDto, List<ValidationError> expected) {

        List<ValidationError> actual = userValidator.validateParams(userSignUpDto);

        assertEquals(expected, actual);
    }

    private static List<Arguments> provideUserParams() {
        List<Arguments> testCases = new ArrayList<>();
        UserSignUpDto signUpDto = provideSignUpDto();

        testCases.add(Arguments.of(signUpDto, Collections.emptyList()));

        signUpDto = provideSignUpDto();
        signUpDto.setLogin(generateString("login", 2));
        testCases.add(Arguments.of(signUpDto, Collections.emptyList()));
        signUpDto = provideSignUpDto();
        signUpDto.setLogin(generateString("login", 50));
        testCases.add(Arguments.of(signUpDto, Collections.emptyList()));
        signUpDto = provideSignUpDto();
        signUpDto.setLogin(generateString("login", 1));
        testCases.add(Arguments.of(signUpDto, Collections.singletonList(TOO_SHORT_USER_LOGIN)));
        signUpDto = provideSignUpDto();
        signUpDto.setLogin(generateString("login", 51));
        testCases.add(Arguments.of(signUpDto, Collections.singletonList(TOO_LONG_USER_LOGIN)));
        signUpDto = provideSignUpDto();
        signUpDto.setLogin("login!");
        testCases.add(Arguments.of(signUpDto, Collections.singletonList(INVALID_USER_LOGIN)));

        signUpDto = provideSignUpDto();
        signUpDto.setPassword(generateString("Pass1234", 8));
        testCases.add(Arguments.of(signUpDto, Collections.emptyList()));
        signUpDto = provideSignUpDto();
        signUpDto.setPassword(generateString("Pass1234", 45));
        testCases.add(Arguments.of(signUpDto, Collections.emptyList()));
        signUpDto = provideSignUpDto();
        signUpDto.setPassword(generateString("Pass1234", 7));
        testCases.add(Arguments.of(signUpDto, Collections.singletonList(TOO_SHORT_USER_PASSWORD)));
        signUpDto = provideSignUpDto();
        signUpDto.setPassword(generateString("Pass1234", 46));
        testCases.add(Arguments.of(signUpDto, Collections.singletonList(TOO_LONG_USER_PASSWORD)));
        signUpDto = provideSignUpDto();
        signUpDto.setPassword("Password");
        testCases.add(Arguments.of(signUpDto, Collections.singletonList(INVALID_USER_PASSWORD)));

        signUpDto = provideSignUpDto();
        signUpDto.setName(generateString("Ivan", 2));
        testCases.add(Arguments.of(signUpDto, Collections.emptyList()));
        signUpDto = provideSignUpDto();
        signUpDto.setName(generateString("Ivan", 45));
        testCases.add(Arguments.of(signUpDto, Collections.emptyList()));
        signUpDto = provideSignUpDto();
        signUpDto.setName(generateString("Ivan", 1));
        testCases.add(Arguments.of(signUpDto, Collections.singletonList(TOO_SHORT_USER_NAME)));
        signUpDto = provideSignUpDto();
        signUpDto.setName(generateString("Ivan", 46));
        testCases.add(Arguments.of(signUpDto, Collections.singletonList(TOO_LONG_USER_NAME)));
        signUpDto = provideSignUpDto();
        signUpDto.setName("Ivan!");
        testCases.add(Arguments.of(signUpDto, Collections.singletonList(INVALID_USER_NAME)));

        signUpDto = provideSignUpDto();
        signUpDto.setSurname(generateString("Ivanov", 2));
        testCases.add(Arguments.of(signUpDto, Collections.emptyList()));
        signUpDto = provideSignUpDto();
        signUpDto.setSurname(generateString("Ivanov", 45));
        testCases.add(Arguments.of(signUpDto, Collections.emptyList()));
        signUpDto = provideSignUpDto();
        signUpDto.setSurname(generateString("Ivanov", 1));
        testCases.add(Arguments.of(signUpDto, Collections.singletonList(TOO_SHORT_USER_SURNAME)));
        signUpDto = provideSignUpDto();
        signUpDto.setSurname(generateString("Ivanov", 46));
        testCases.add(Arguments.of(signUpDto, Collections.singletonList(TOO_LONG_USER_SURNAME)));
        signUpDto = provideSignUpDto();
        signUpDto.setSurname("Ivanov!");
        testCases.add(Arguments.of(signUpDto, Collections.singletonList(INVALID_USER_SURNAME)));

        signUpDto = provideSignUpDto();
        signUpDto.setEmail("ivanov@gmail.com");
        testCases.add(Arguments.of(signUpDto, Collections.emptyList()));
        signUpDto = provideSignUpDto();
        signUpDto.setEmail("a@b.c");
        testCases.add(Arguments.of(signUpDto, Collections.emptyList()));
        signUpDto = provideSignUpDto();
        signUpDto.setEmail(generateString("ivan", 245) + "@gmail.com");
        testCases.add(Arguments.of(signUpDto, Collections.emptyList()));
        signUpDto = provideSignUpDto();
        signUpDto.setEmail("a");
        testCases.add(Arguments.of(signUpDto, Collections.singletonList(TOO_SHORT_USER_EMAIL)));
        signUpDto = provideSignUpDto();
        signUpDto.setEmail(generateString("ivan", 246) + "@gmail.com");
        testCases.add(Arguments.of(signUpDto, Collections.singletonList(TOO_LONG_USER_EMAIL)));
        signUpDto = provideSignUpDto();
        signUpDto.setEmail("@gmail.com");
        testCases.add(Arguments.of(signUpDto, Collections.singletonList(INVALID_USER_EMAIL)));
        signUpDto.setEmail("ivanov@gmail");
        testCases.add(Arguments.of(signUpDto, Collections.singletonList(INVALID_USER_EMAIL)));
        signUpDto.setEmail("gmail");
        testCases.add(Arguments.of(signUpDto, Collections.singletonList(INVALID_USER_EMAIL)));

        return testCases;
    }

    private static UserSignUpDto provideSignUpDto() {
        return UserSignUpDto.builder()
                .login("user_test")
                .password("Password1234")
                .name("Alex")
                .surname("Piterson")
                .email("alex.piterson@gmail.com")
                .build();
    }

    private static String generateString(String line, int length) {
        StringBuilder result = new StringBuilder(line);
        while (result.length() < length){
            result.append(line);
        }
        return result.substring(0, length);
    }
}
