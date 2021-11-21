package com.epam.esm.controller;

import com.epam.esm.dto.*;
import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.InvalidEntityException;
import com.epam.esm.exception.InvalidPaginationException;
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

import static com.epam.esm.validator.ValidationError.PAGE_IS_OUT_OF_RANGE;
import static com.epam.esm.validator.ValidationError.TOO_BIG_PAGE_SIZE;
import static com.epam.esm.validator.ValidationError.TOO_SMALL_PAGE_NUMBER;
import static com.epam.esm.validator.ValidationError.TOO_SMALL_PAGE_SIZE;
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
    private static final String INVALID_PAGINATION_MESSAGE = "invalid_pagination";
    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "internal_server_error";

    private static final String TAG_MESSAGE = "entities.tag";
    private static final String GIFT_CERTIFICATE_MESSAGE = "entities.gift_certificate";
    private static final String ORDER_CREATE_REQUEST_MESSAGE = "entities.order_create_request";
    private static final String ORDER_ITEM_MESSAGE = "entities.order_item";
    private static final String ORDER_RESPONSE_MESSAGE = "entities.order_response";
    private static final String ORDER_UPDATE_REQUEST_MESSAGE = "entities.order_update_request";
    private static final String USER_MESSAGE = "entities.user";
    private static final String USER_ORDER_RESPONSE_MESSAGE = "entities.user_order_response";
    private static final String ENTITY_PLACEHOLDER_MESSAGE = "entities.placeholder";

    private static final String TAG_ALREADY_EXISTS_MESSAGE = "entity_already_exists.tag_message";
    private static final String GIFT_CERTIFICATE_ALREADY_EXISTS_MESSAGE = "entity_already_exists.gift_certificate_message";

    private static final String TAG_NAME_REQUIRED_MESSAGE = "invalid_entity.tag_name_required";
    private static final String GIFT_CERTIFICATE_NAME_REQUIRED_MESSAGE = "invalid_entity.gift_certificate_name_required";
    private static final String GIFT_CERTIFICATE_DESCRIPTION_REQUIRED_MESSAGE = "invalid_entity.gift_certificate_description_required";
    private static final String GIFT_CERTIFICATE_PRICE_REQUIRED_MESSAGE = "invalid_entity.gift_certificate_price_required";
    private static final String GIFT_CERTIFICATE_DURATION_REQUIRED_MESSAGE = "invalid_entity.gift_certificate_duration_required";
    private static final String ORDER_USER_REQUIRED_MESSAGE = "invalid_entity.order_user_required";
    private static final String ORDER_ITEMS_REQUIRED_MESSAGE = "invalid_entity.order_items_required";

    private static final String IMPOSSIBLE_TO_UPDATE_SEVERAL_GIFT_CERTIFICATE_FIELDS_MESSAGE = "invalid_entity.impossible_to_update_several_gift_certificate_fields";
    private static final String NO_GIFT_CERTIFICATE_FIELDS_TO_UPDATE_MESSAGE = "invalid_entity.no_gift_certificate_fields_to_update";

    private static final String INVALID_ORDER_ITEMS_AMOUNT_MESSAGE = "invalid_entity.invalid_order_items_amount";
    private static final String NOT_UNIQUE_GIFT_CERTIFICATES_IN_ORDER_MESSAGE = "invalid_entity.not_unique_gift_certificates_in_order";

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
    private static final String INVALID_ORDER_ORDERING_TYPE_MESSAGE = "invalid_entity.invalid_order_ordering_type";

    private static final String TOO_SMALL_PAGE_NUMBER_MESSAGE = "invalid_entity.too_small_page_number";

    private static final String TOO_SMALL_PAGE_SIZE_MESSAGE = "invalid_entity.too_small_page_size";
    private static final String TOO_BIG_PAGE_SIZE_MESSAGE = "invalid_entity.too_big_page_size";

    private static final String PAGE_IS_OUT_OF_RANGE_MESSAGE = "invalid_entity.page_is_out_of_range";

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

    @ExceptionHandler(InvalidPaginationException.class)
    public ResponseEntity<Object> handleEntityNotFound(InvalidPaginationException e) {
        List<ValidationError> validationErrors = e.getPaginationErrors();
        StringBuilder errorLine = new StringBuilder();
        for (ValidationError error: validationErrors) {
            switch (error) {
                case TOO_SMALL_PAGE_NUMBER: {
                    errorLine.append(getMessage(TOO_SMALL_PAGE_NUMBER_MESSAGE));
                    break;
                }

                case TOO_SMALL_PAGE_SIZE: {
                    errorLine.append(getMessage(TOO_SMALL_PAGE_SIZE_MESSAGE));
                    break;
                }
                case TOO_BIG_PAGE_SIZE: {
                    errorLine.append(getMessage(TOO_BIG_PAGE_SIZE_MESSAGE));
                    break;
                }

                case PAGE_IS_OUT_OF_RANGE: {
                    errorLine.append(getMessage(PAGE_IS_OUT_OF_RANGE_MESSAGE));
                    break;
                }
            }
            errorLine.append(ERROR_SEPARATOR);
        }
        int lastSeparatorPos = errorLine.length() - ERROR_SEPARATOR.length();
        errorLine.replace(lastSeparatorPos, errorLine.length(), "");

        String errorMessage = String.format(getMessage(INVALID_PAGINATION_MESSAGE),
                e.getPageNumber(), e.getPageSize(), errorLine);
        return buildErrorResponseEntity(BAD_REQUEST, errorMessage);
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
                case ORDER_USER_REQUIRED: {
                    errorLine.append(getMessage(ORDER_USER_REQUIRED_MESSAGE));
                    break;
                }
                case ORDER_ITEMS_REQUIRED: {
                    errorLine.append(getMessage(ORDER_ITEMS_REQUIRED_MESSAGE));
                    break;
                }

                case IMPOSSIBLE_TO_UPDATE_SEVERAL_GIFT_CERTIFICATE_FIELDS: {
                    errorLine.append(getMessage(IMPOSSIBLE_TO_UPDATE_SEVERAL_GIFT_CERTIFICATE_FIELDS_MESSAGE));
                    break;
                }
                case NO_GIFT_CERTIFICATE_FIELDS_TO_UPDATE: {
                    errorLine.append(getMessage(NO_GIFT_CERTIFICATE_FIELDS_TO_UPDATE_MESSAGE));
                    break;
                }

                case INVALID_ORDER_ITEMS_AMOUNT: {
                    errorLine.append(getMessage(INVALID_ORDER_ITEMS_AMOUNT_MESSAGE));
                    break;
                }
                case NOT_UNIQUE_GIFT_CERTIFICATES_IN_ORDER: {
                    errorLine.append(getMessage(NOT_UNIQUE_GIFT_CERTIFICATES_IN_ORDER_MESSAGE));
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
                case INVALID_ORDER_ORDERING_TYPE: {
                    errorLine.append(getMessage(INVALID_ORDER_ORDERING_TYPE_MESSAGE));
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
            entityName = getMessage(TAG_MESSAGE);
        }
        else if (GiftCertificateDto.class.equals(entity)) {
            entityName = getMessage(GIFT_CERTIFICATE_MESSAGE);
        }
        else if (OrderCreateRequestDto.class.equals(entity)) {
            entityName = getMessage(ORDER_CREATE_REQUEST_MESSAGE);
        }
        else if (OrderItemDto.class.equals(entity)) {
            entityName = getMessage(ORDER_ITEM_MESSAGE);
        }
        else if (OrderResponseDto.class.equals(entity)) {
            entityName = getMessage(ORDER_RESPONSE_MESSAGE);
        }
        else if (OrderUpdateRequestDto.class.equals(entity)) {
            entityName = getMessage(ORDER_UPDATE_REQUEST_MESSAGE);
        }
        else if (UserDto.class.equals(entity)) {
            entityName = getMessage(USER_MESSAGE);
        }
        else if (UserOrderResponseDto.class.equals(entity)) {
            entityName = getMessage(USER_ORDER_RESPONSE_MESSAGE);
        }
        return entityName;
    }

    private ResponseEntity<Object> buildErrorResponseEntity(HttpStatus status, String errorMessage) {
        Map<String, Object> body = new HashMap<>();
        body.put(ERROR_MESSAGE, errorMessage);

        return new ResponseEntity<>(body, status);
    }
}
