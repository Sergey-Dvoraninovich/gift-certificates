package com.epam.esm.validator;

import com.epam.esm.dto.UserSignUpDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.epam.esm.validator.ValidationError.*;

@Component
public class UserValidator {
    private static final String LOGIN_REGEXP = "^[A-Za-z_]{2,50}$";
    private static final String PASSWORD_REGEXP = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{8,}$";
    private static final String NAME_REGEXP = "^[A-Za-z]{2,45}$";
    private static final String SURNAME_REGEXP = "^[A-Za-z]{2,45}$";
    private static final String EMAIL_REGEXP = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

    private static final int LOGIN_MIN_LENGTH = 2;
    private static final int LOGIN_MAX_LENGTH = 50;

    private static final int PASSWORD_MIN_LENGTH = 8;
    private static final int PASSWORD_MAX_LENGTH = 45;

    private static final int NAME_MIN_LENGTH = 2;
    private static final int NAME_MAX_LENGTH = 45;

    private static final int SURNAME_MIN_LENGTH = 2;
    private static final int SURNAME_MAX_LENGTH = 45;

    private static final int EMAIL_MIN_LENGTH = 5;
    private static final int EMAIL_MAX_LENGTH = 255;

    public List<ValidationError> validateWithRequiredParams(UserSignUpDto userSignUpDto) {
        List<ValidationError> validationErrors = new ArrayList<>();
        if (userSignUpDto.getLogin() == null){
            validationErrors.add(USER_LOGIN_REQUIRED);
        }
        if (userSignUpDto.getPassword() == null){
            validationErrors.add(USER_PASSWORD_REQUIRED);
        }
        if (userSignUpDto.getName() == null){
            validationErrors.add(USER_NAME_REQUIRED);
        }
        if (userSignUpDto.getSurname() == null){
            validationErrors.add(USER_SURNAME_REQUIRED);
        }
        if (userSignUpDto.getEmail() == null){
            validationErrors.add(USER_EMAIL_REQUIRED);
        }
        if (validationErrors.isEmpty()) {
            validationErrors.addAll(validateParams(userSignUpDto));
        }
        return validationErrors;
    }

    public List<ValidationError> validateParams(UserSignUpDto userSignUpDto) {
        List<ValidationError> validationErrors = new ArrayList<>();

        validateLogin(userSignUpDto, validationErrors);
        validatePassword(userSignUpDto, validationErrors);
        validateName(userSignUpDto, validationErrors);
        validateSurname(userSignUpDto, validationErrors);
        validateEmail(userSignUpDto, validationErrors);

        return validationErrors;
    }

    private void validateLogin(UserSignUpDto userSignUpDto, List<ValidationError> validationErrors) {
        String login = userSignUpDto.getLogin();
        if (login != null) {
            if (login.length() < LOGIN_MIN_LENGTH) {
                validationErrors.add(TOO_SHORT_USER_LOGIN);
            }
            else if (login.length() > LOGIN_MAX_LENGTH) {
                validationErrors.add(TOO_LONG_USER_LOGIN);
            }
            else if (!Pattern.matches(LOGIN_REGEXP, login)) {
                validationErrors.add(INVALID_USER_LOGIN);
            }
        }
    }

    private void validatePassword(UserSignUpDto userSignUpDto, List<ValidationError> validationErrors) {
        String password = userSignUpDto.getPassword();
        if (password != null) {
            if (password.length() < PASSWORD_MIN_LENGTH) {
                validationErrors.add(TOO_SHORT_USER_PASSWORD);
            }
            else if (password.length() > PASSWORD_MAX_LENGTH) {
                validationErrors.add(TOO_LONG_USER_PASSWORD);
            }
            else if (!Pattern.matches(PASSWORD_REGEXP, password)) {
                validationErrors.add(INVALID_USER_PASSWORD);
            }
        }
    }

    private void validateName(UserSignUpDto userSignUpDto, List<ValidationError> validationErrors) {
        String name = userSignUpDto.getName();
        if (name != null) {
            if (name.length() < NAME_MIN_LENGTH) {
                validationErrors.add(TOO_SHORT_USER_NAME);
            }
            else if (name.length() > NAME_MAX_LENGTH) {
                validationErrors.add(TOO_LONG_USER_NAME);
            }
            else if (!Pattern.matches(NAME_REGEXP, name)) {
                validationErrors.add(INVALID_USER_NAME);
            }
        }
    }

    private void validateSurname(UserSignUpDto userSignUpDto, List<ValidationError> validationErrors) {
        String surname = userSignUpDto.getSurname();
        if (surname != null) {
            if (surname.length() < SURNAME_MIN_LENGTH) {
                validationErrors.add(TOO_SHORT_USER_SURNAME);
            }
            else if (surname.length() > SURNAME_MAX_LENGTH) {
                validationErrors.add(TOO_LONG_USER_SURNAME);
            }
            else if (!Pattern.matches(SURNAME_REGEXP, surname)) {
                validationErrors.add(INVALID_USER_SURNAME);
            }
        }
    }

    private void validateEmail(UserSignUpDto userSignUpDto, List<ValidationError> validationErrors) {
        String email = userSignUpDto.getEmail();
        if (email != null) {
            if (email.length() < EMAIL_MIN_LENGTH) {
                validationErrors.add(TOO_SHORT_USER_EMAIL);
            }
            else if (email.length() > EMAIL_MAX_LENGTH) {
                validationErrors.add(TOO_LONG_USER_EMAIL);
            }
            else if (!Pattern.matches(EMAIL_REGEXP, email)) {
                validationErrors.add(INVALID_USER_EMAIL);
            }
        }
    }
}
