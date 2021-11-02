package com.epam.esm.validator;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.epam.esm.validator.ValidationError.*;

@Component
public class TagValidator {
    private static final String NAME_NO_LEADING_SYMBOLS_REGEXP = "^[A-Za-z]{1}[A-Za-z\\s]{1,498}[A-Za-z]{1}$";
    private static final String NAME_SYMBOLS_REGEXP = "^[A-Za-z\\s]{3,45}$";
    private static final int NAME_MIN_LENGTH = 3;
    private static final int NAME_MAX_LENGTH = 45;

    public List<ValidationError> validate(String name) {
        List<ValidationError> validationErrors = new ArrayList<>();

        if (name != null) {
            if (name.length() < NAME_MIN_LENGTH) {
                validationErrors.add(TOO_SHORT_NAME);
            }
            else if (name.length() > NAME_MAX_LENGTH) {
                validationErrors.add(TOO_LONG_NAME);
            }
            else {
                if (!Pattern.matches(NAME_NO_LEADING_SYMBOLS_REGEXP, name)
                        && Pattern.matches(NAME_SYMBOLS_REGEXP, name)) {
                    validationErrors.add(INVALID_LEADING_OR_CLOSING_SYMBOLS_IN_NAME);
                }
                if (!Pattern.matches(NAME_SYMBOLS_REGEXP, name)) {
                    validationErrors.add(INVALID_SYMBOLS_IN_NAME);
                }
            }
        }

        return validationErrors;
    }

}
