package com.epam.esm.controller;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.InvalidEntityException;
import com.epam.esm.validator.ValidationError;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
@RequiredArgsConstructor
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {
    private final ResourceBundleMessageSource messageSource;

    private static final Logger logger = LogManager.getLogger();

    private static final String ERROR_MESSAGE = "errorMessage";

    private static final String ENTITY_ALREADY_EXISTS_MESSAGE = "entity_already_exists";
    private static final String ENTITY_NOT_FOUND_MESSAGE = "entity_not_found";
    private static final String INVALID_ENTITY_MESSAGE = "invalid_entity";
    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "internal_server_error";

    private static final String TAG_ENTITY_NAME_MESSAGE = "entities.tag";
    private static final String GIFT_CERTIFICATE_ENTITY_NAME_MESSAGE = "entities.gift_certificate";
    private static final String ENTITY_PLACEHOLDER_MESSAGE = "entities.placeholder";

    private static final String TAG_ALREADY_EXISTS_MESSAGE = "entity_already_exists.tag_message";
    private static final String GIFT_CERTIFICATE_ALREADY_EXISTS_MESSAGE = "entity_already_exists.gift_certificate_message";

    private static final String TAG_NAME_REQUIRED_MESSAGE = "invalid_entity.tag_name_required";
    private static final String GIFT_CERTIFICATE_NAME_REQUIRED_MESSAGE = "invalid_entity.gift_certificate_name_required";
    private static final String GIFT_CERTIFICATE_DESCRIPTION_REQUIRED_MESSAGE = "invalid_entity.gift_certificate_description_required";
    private static final String GIFT_CERTIFICATE_PRICE_REQUIRED_MESSAGE = "invalid_entity.gift_certificate_price_required";
    private static final String GIFT_CERTIFICATE_DURATION_REQUIRED_MESSAGE = "invalid_entity.gift_certificate_duration_required";

    private static final String TOO_LONG_TAG_NAME_MESSAGE = "invalid_entity.tag_too_long_name";
    private static final String TOO_SHORT_TAG_NAME_MESSAGE = "invalid_entity.tag_too_short_name";
    private static final String INVALID_TAG_NAME_MESSAGE = "invalid_entity.tag_invalid_name";
    private static final String INVALID_LEADING_OR_CLOSING_SYMBOLS_IN_TAG_NAME_MESSAGE = "invalid_entity.tag_invalid_leading_or_closing_symbols_in_name";

    private static final String TOO_LONG_GIFT_CERTIFICATE_NAME_MESSAGE = "invalid_entity.gift_certificate_too_long_name";
    private static final String TOO_SHORT_GIFT_CERTIFICATE_NAME_MESSAGE = "invalid_entity.gift_certificate_too_short_name";
    private static final String INVALID_GIFT_CERTIFICATE_NAME_MESSAGE = "invalid_entity.gift_certificate_invalid_name";
    private static final String INVALID_LEADING_OR_CLOSING_SYMBOLS_IN_GIFT_CERTIFICATE_NAME_MESSAGE = "invalid_entity.gift_certificate_invalid_leading_or_closing_symbols_in_name";

    private static final String TOO_SHORT_GIFT_CERTIFICATE_DESCRIPTION_MESSAGE = "invalid_entity.gift_certificate_too_short_description";
    private static final String TOO_LONG_GIFT_CERTIFICATE_DESCRIPTION_MESSAGE = "invalid_entity.gift_certificate_too_long_description";
    private static final String INVALID_SYMBOLS_IN_GIFT_CERTIFICATE_DESCRIPTION_MESSAGE = "invalid_entity.gift_certificate_invalid_symbols_in_description";
    private static final String INVALID_LEADING_OR_CLOSING_SYMBOLS_IN_GIFT_CERTIFICATE_DESCRIPTION_MESSAGE = "invalid_entity.gift_certificate_invalid_leading_or_closing_symbols_in_description";

    private static final String TOO_SHORT_GIFT_CERTIFICATE_DURATION_MESSAGE = "invalid_entity.gift_certificate_too_short_duration";
    private static final String TOO_LONG_GIFT_CERTIFICATE_DURATION_MESSAGE = "invalid_entity.gift_certificate_too_long_duration";
    private static final String INVALID_SYMBOLS_IN_GIFT_CERTIFICATE_DURATION_MESSAGE = "invalid_entity.gift_certificate_invalid_symbols_in_duration";

    private static final String TOO_SMALL_GIFT_CERTIFICATE_PRICE_MESSAGE = "invalid_entity.gift_certificate_too_small_price";
    private static final String TOO_BIG_GIFT_CERTIFICATE_PRICE_MESSAGE = "invalid_entity.gift_certificate_too_big_price";
    private static final String INVALID_GIFT_CERTIFICATE_PRICE_FORMAT_MESSAGE = "invalid_entity.gift_certificate_invalid_price_format";

    private static final String INVALID_NAME_ORDERING_TYPE_MESSAGE = "invalid_entity.invalid_name_ordering_type";
    private static final String INVALID_CREATE_DATE_ORDERING_TYPE_MESSAGE = "invalid_entity.invalid_create_date_ordering_type";

    private static final String ERROR_SEPARATOR = ", ";

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<Object> handleEntityAlreadyExists(EntityAlreadyExistsException e) {
        Class<?> entityClass = e.getCauseEntity();
        String entityName = getEntityMessage(entityClass, ENTITY_PLACEHOLDER_MESSAGE);

        String message = "";
        if (TagDto.class.equals(entityClass)) {
            message = " (" + getMessage(TAG_ALREADY_EXISTS_MESSAGE) + ")";
        } else if (GiftCertificateDto.class.equals(entityClass)) {
            message = " (" + getMessage(GIFT_CERTIFICATE_ALREADY_EXISTS_MESSAGE) + ")";
        }

        String errorMessage = String.format(getMessage(ENTITY_ALREADY_EXISTS_MESSAGE), entityName, message);
        return buildErrorResponseEntity(CONFLICT, errorMessage);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException e) {
        Class<?> entityClass = e.getCauseEntity();
        String entityName = getEntityMessage(entityClass, ENTITY_PLACEHOLDER_MESSAGE);

        String errorMessage = String.format(getMessage(ENTITY_NOT_FOUND_MESSAGE), entityName, e.getEntityId());
        return buildErrorResponseEntity(NOT_FOUND, errorMessage);
    }

    @ExceptionHandler(InvalidEntityException.class)
    public ResponseEntity<Object> handleInvalidEntity(InvalidEntityException e) {
        List<ValidationError> validationErrors = e.getValidationErrors();
        StringBuilder errorLine = new StringBuilder();

        for (ValidationError error: validationErrors) {
            switch (error) {
                case TAG_NAME_REQUIRED: {
                    errorLine.append(getMessage(TAG_NAME_REQUIRED_MESSAGE));
                    break;
                }
                case GIFT_CERTIFICATE_NAME_REQUIRED: {
                    errorLine.append(getMessage(GIFT_CERTIFICATE_NAME_REQUIRED_MESSAGE));
                    break;
                }
                case GIFT_CERTIFICATE_DESCRIPTION_REQUIRED: {
                    errorLine.append(getMessage(GIFT_CERTIFICATE_DESCRIPTION_REQUIRED_MESSAGE));
                    break;
                }
                case GIFT_CERTIFICATE_PRICE_REQUIRED: {
                    errorLine.append(getMessage(GIFT_CERTIFICATE_PRICE_REQUIRED_MESSAGE));
                    break;
                }
                case GIFT_CERTIFICATE_DURATION_REQUIRED: {
                    errorLine.append(getMessage(GIFT_CERTIFICATE_DURATION_REQUIRED_MESSAGE));
                    break;
                }

                case TOO_SHORT_TAG_NAME: {
                    errorLine.append(getMessage(TOO_SHORT_TAG_NAME_MESSAGE));
                    break;
                }
                case TOO_LONG_TAG_NAME: {
                    errorLine.append(getMessage(TOO_LONG_TAG_NAME_MESSAGE));
                    break;
                }
                case INVALID_SYMBOLS_IN_TAG_NAME: {
                    errorLine.append(getMessage(INVALID_TAG_NAME_MESSAGE));
                    break;
                }
                case INVALID_LEADING_OR_CLOSING_SYMBOLS_IN_TAG_NAME: {
                    errorLine.append(getMessage(INVALID_LEADING_OR_CLOSING_SYMBOLS_IN_TAG_NAME_MESSAGE));
                    break;
                }

                case TOO_SHORT_GIFT_CERTIFICATE_NAME: {
                    errorLine.append(getMessage(TOO_SHORT_GIFT_CERTIFICATE_NAME_MESSAGE));
                    break;
                }
                case TOO_LONG_GIFT_CERTIFICATE_NAME: {
                    errorLine.append(getMessage(TOO_LONG_GIFT_CERTIFICATE_NAME_MESSAGE));
                    break;
                }
                case INVALID_SYMBOLS_IN_GIFT_CERTIFICATE_NAME: {
                    errorLine.append(getMessage(INVALID_GIFT_CERTIFICATE_NAME_MESSAGE));
                    break;
                }
                case INVALID_LEADING_OR_CLOSING_SYMBOLS_IN_GIFT_CERTIFICATE_NAME: {
                    errorLine.append(getMessage(INVALID_LEADING_OR_CLOSING_SYMBOLS_IN_GIFT_CERTIFICATE_NAME_MESSAGE));
                    break;
                }

                case TOO_SHORT_GIFT_CERTIFICATE_DESCRIPTION: {
                    errorLine.append(getMessage(TOO_SHORT_GIFT_CERTIFICATE_DESCRIPTION_MESSAGE));
                    break;
                }
                case TOO_LONG_GIFT_CERTIFICATE_DESCRIPTION: {
                    errorLine.append(getMessage(TOO_LONG_GIFT_CERTIFICATE_DESCRIPTION_MESSAGE));
                    break;
                }
                case INVALID_SYMBOLS_IN_GIFT_CERTIFICATE_DESCRIPTION: {
                    errorLine.append(getMessage(INVALID_SYMBOLS_IN_GIFT_CERTIFICATE_DESCRIPTION_MESSAGE));
                    break;
                }
                case INVALID_LEADING_OR_CLOSING_SYMBOLS_IN_GIFT_CERTIFICATE_DESCRIPTION: {
                    errorLine.append(getMessage(INVALID_LEADING_OR_CLOSING_SYMBOLS_IN_GIFT_CERTIFICATE_DESCRIPTION_MESSAGE));
                    break;
                }

                case TOO_SMALL_GIFT_CERTIFICATE_PRICE: {
                    errorLine.append(getMessage(TOO_SMALL_GIFT_CERTIFICATE_PRICE_MESSAGE));
                    break;
                }
                case TOO_BIG_GIFT_CERTIFICATE_PRICE: {
                    errorLine.append(getMessage(TOO_BIG_GIFT_CERTIFICATE_PRICE_MESSAGE));
                    break;
                }
                case INVALID_GIFT_CERTIFICATE_PRICE_FORMAT: {
                    errorLine.append(getMessage(INVALID_GIFT_CERTIFICATE_PRICE_FORMAT_MESSAGE));
                    break;
                }

                case TOO_SHORT_GIFT_CERTIFICATE_DURATION: {
                    errorLine.append(getMessage(TOO_SHORT_GIFT_CERTIFICATE_DURATION_MESSAGE));
                    break;
                }
                case TOO_LONG_GIFT_CERTIFICATE_DURATION: {
                    errorLine.append(getMessage(TOO_LONG_GIFT_CERTIFICATE_DURATION_MESSAGE));
                    break;
                }
                case INVALID_SYMBOLS_IN_GIFT_CERTIFICATE_DURATION: {
                    errorLine.append(getMessage(INVALID_SYMBOLS_IN_GIFT_CERTIFICATE_DURATION_MESSAGE));
                    break;
                }

                case INVALID_NAME_ORDERING_TYPE: {
                    errorLine.append(getMessage(INVALID_NAME_ORDERING_TYPE_MESSAGE));
                    break;
                }
                case INVALID_CREATE_DATE_ORDERING_TYPE: {
                    errorLine.append(getMessage(INVALID_CREATE_DATE_ORDERING_TYPE_MESSAGE));
                    break;
                }
            }
            errorLine.append(ERROR_SEPARATOR);
        }
        int lastSeparatorPos = errorLine.length() - ERROR_SEPARATOR.length();
        errorLine.replace(lastSeparatorPos, errorLine.length(), "");

        String errorMessage = String.format(getMessage(INVALID_ENTITY_MESSAGE), errorLine);
        return buildErrorResponseEntity(BAD_REQUEST, errorMessage);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleDefault(Exception e) {
        logger.error("Exception appeared: ", e);
        String errorMessage = getMessage(INTERNAL_SERVER_ERROR_MESSAGE);
        return buildErrorResponseEntity(INTERNAL_SERVER_ERROR, errorMessage);
    }

    private String getMessage(String errorMessageName) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(errorMessageName, null, locale);
    }

    private String getEntityMessage(Class<?> entity, String placeholderName) {
        String entityName = getMessage(placeholderName);
        if (TagDto.class.equals(entity)) {
            entityName = getMessage(TAG_ENTITY_NAME_MESSAGE);
        } else if (GiftCertificateDto.class.equals(entity)) {
            entityName = getMessage(GIFT_CERTIFICATE_ENTITY_NAME_MESSAGE);
        }
        return entityName;
    }

    private ResponseEntity<Object> buildErrorResponseEntity(HttpStatus status, String errorMessage) {
        Map<String, Object> body = new HashMap<>();
        body.put(ERROR_MESSAGE, errorMessage);

        return new ResponseEntity<>(body, status);
    }
}
