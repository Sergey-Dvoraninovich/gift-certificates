package com.epam.esm.validator;

import com.epam.esm.dto.TagDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.epam.esm.validator.ValidationError.INVALID_LEADING_OR_CLOSING_SYMBOLS_IN_TAG_NAME;
import static com.epam.esm.validator.ValidationError.INVALID_SYMBOLS_IN_TAG_NAME;
import static com.epam.esm.validator.ValidationError.TAG_NAME_REQUIRED;
import static com.epam.esm.validator.ValidationError.TOO_LONG_TAG_NAME;
import static com.epam.esm.validator.ValidationError.TOO_SHORT_TAG_NAME;

@Component
public class TagValidator {
    private static final String NAME_NO_LEADING_SYMBOLS_REGEXP = "^[A-Za-z]{1}[A-Za-z\\s]{0,43}[A-Za-z]{1}$";
    private static final String NAME_SYMBOLS_REGEXP = "^[A-Za-z\\s]{2,45}$";
    private static final int NAME_MIN_LENGTH = 2;
    private static final int NAME_MAX_LENGTH = 45;

    public List<ValidationError> validateWithRequiredParams(TagDto tagDto) {
        List<ValidationError> validationErrors = new ArrayList<>();
        if (tagDto.getName() == null){
            validationErrors.add(TAG_NAME_REQUIRED);
        }
        if (validationErrors.isEmpty()) {
            validationErrors.addAll(validateParams(tagDto.getName()));
        }
        return validationErrors;
    }

    public List<ValidationError> validateParams(String name) {
        List<ValidationError> validationErrors = new ArrayList<>();

        if (name != null) {
            if (name.length() < NAME_MIN_LENGTH) {
                validationErrors.add(TOO_SHORT_TAG_NAME);
            }
            else if (name.length() > NAME_MAX_LENGTH) {
                validationErrors.add(TOO_LONG_TAG_NAME);
            }
            else {
                if (!Pattern.matches(NAME_NO_LEADING_SYMBOLS_REGEXP, name)
                        && Pattern.matches(NAME_SYMBOLS_REGEXP, name)) {
                    validationErrors.add(INVALID_LEADING_OR_CLOSING_SYMBOLS_IN_TAG_NAME);
                }
                if (!Pattern.matches(NAME_SYMBOLS_REGEXP, name)) {
                    validationErrors.add(INVALID_SYMBOLS_IN_TAG_NAME);
                }
            }
        }

        return validationErrors;
    }

}
