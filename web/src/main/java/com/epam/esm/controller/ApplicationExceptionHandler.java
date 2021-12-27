package com.epam.esm.controller;

import com.epam.esm.dto.GiftCertificateFilterDto;
import com.epam.esm.dto.GiftCertificateResponseDto;
import com.epam.esm.dto.OrderCreateRequestDto;
import com.epam.esm.dto.OrderItemDto;
import com.epam.esm.dto.OrderResponseDto;
import com.epam.esm.dto.OrderUpdateRequestDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.TokenDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.UserOrderResponseDto;
import com.epam.esm.dto.UserSignInDto;
import com.epam.esm.dto.UserSignUpDto;
import com.epam.esm.exception.AccessException;
import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.exception.EntityNotAvailableException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.ExceptionResponse;
import com.epam.esm.exception.InvalidEntityException;
import com.epam.esm.exception.InvalidPaginationException;
import com.epam.esm.exception.JwtTokenException;
import com.epam.esm.exception.RefreshTokenException;
import com.epam.esm.exception.UserAuthenticationException;
import com.epam.esm.validator.ValidationError;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static com.epam.esm.exception.RefreshTokenException.State.INVALID_TOKEN;
import static com.epam.esm.exception.UserAuthenticationException.State.INVALID_LOGIN;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
@RequiredArgsConstructor
public class ApplicationExceptionHandler {
    private final ResourceBundleMessageSource messageSource;

    private static final Logger logger = LogManager.getLogger();

    private static final String ENTITY_ALREADY_EXISTS_MESSAGE = "entity_already_exists";
    private static final String ENTITY_NOT_FOUND_MESSAGE = "entity_not_found";
    private static final String ENTITY_NOT_AVAILABLE_MESSAGE = "entity_not_available";
    private static final String INVALID_ENTITY_MESSAGE = "invalid_entity";
    private static final String INVALID_PAGINATION_MESSAGE = "invalid_pagination";
    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "internal_server_error";
    private static final String INVALID_REQUEST_MESSAGE = "invalid_request";
    private static final String BODY_CANT_BE_EMPTY_MESSAGE = "body_cant_be_empty";
    private static final String ACCESS_DENIED_MESSAGE = "access_denied";
    private static final String BAD_REQUEST_MESSAGE = "bad_request";
    private static final String REQUESTED_METHOD_NOT_AVAILABLE_MESSAGE = "requested_method_not_available";

    private static final String TAG_MESSAGE = "entities.tag";
    private static final String GIFT_CERTIFICATE_MESSAGE = "entities.gift_certificate";
    private static final String GIFT_CERTIFICATE_FILTER_MESSAGE = "entities.gift_certificate_filter";
    private static final String ORDER_CREATE_REQUEST_MESSAGE = "entities.order_create_request";
    private static final String ORDER_ITEM_MESSAGE = "entities.order_item";
    private static final String ORDER_RESPONSE_MESSAGE = "entities.order_response";
    private static final String ORDER_UPDATE_REQUEST_MESSAGE = "entities.order_update_request";
    private static final String USER_MESSAGE = "entities.user";
    private static final String TOKEN_MESSAGE = "entities.token";
    private static final String USER_SIGN_IN_MESSAGE = "entities.user_sign_in";
    private static final String USER_SIGN_UP_MESSAGE = "entities.user_sign_up";
    private static final String USER_ORDER_RESPONSE_MESSAGE = "entities.user_order_response";
    private static final String ENTITY_PLACEHOLDER_MESSAGE = "entities.placeholder";

    private static final String INVALID_ARTIFACT_MESSAGE = "not_found.invalid_artifact";
    private static final String INVALID_VERSION_MESSAGE = "not_found.invalid_version";
    private static final String NOT_FOUND_MESSAGE = "not_found";

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

    private static final String INVALID_TAGS_AMOUNT_MESSAGE = "invalid_entity.invalid_tags_amount";
    private static final String NOT_UNIQUE_TAGS_IN_GIFT_CERTIFICATE_MESSAGE = "invalid_entity.not_unique_tags_in_gift_certificate";

    private static final String INVALID_NAME_ORDERING_TYPE_MESSAGE = "invalid_entity.invalid_name_ordering_type";
    private static final String INVALID_CREATE_DATE_ORDERING_TYPE_MESSAGE = "invalid_entity.invalid_create_date_ordering_type";
    private static final String INVALID_ORDER_ORDERING_TYPE_MESSAGE = "invalid_entity.invalid_order_ordering_type";

    private static final String NOT_UNIQUE_USER_LOGIN_MESSAGE = "invalid_entity.not_unique_user_login";
    private static final String INVALID_USER_LOGIN_MESSAGE = "invalid_entity.invalid_user_login";
    private static final String TOO_SHORT_USER_LOGIN_MESSAGE = "invalid_entity.too_short_user_login";
    private static final String TOO_LONG_USER_LOGIN_MESSAGE = "invalid_entity.too_long_user_login";

    private static final String INVALID_USER_PASSWORD_MESSAGE = "invalid_entity.invalid_user_password";
    private static final String TOO_SHORT_USER_PASSWORD_MESSAGE = "invalid_entity.too_short_user_password";
    private static final String TOO_LONG_USER_PASSWORD_MESSAGE = "invalid_entity.too_long_user_password";

    private static final String INVALID_USER_NAME_MESSAGE = "invalid_entity.invalid_user_name";
    private static final String TOO_SHORT_USER_NAME_MESSAGE = "invalid_entity.too_short_user_name";
    private static final String TOO_LONG_USER_NAME_MESSAGE = "invalid_entity.too_long_user_name";

    private static final String INVALID_USER_SURNAME_MESSAGE = "invalid_entity.invalid_user_surname";
    private static final String TOO_SHORT_USER_SURNAME_MESSAGE = "invalid_entity.too_short_user_surname";
    private static final String TOO_LONG_USER_SURNAME_MESSAGE = "invalid_entity.too_long_user_surname";

    private static final String INVALID_USER_EMAIL_MESSAGE = "invalid_entity.invalid_user_email";
    private static final String TOO_SHORT_USER_EMAIL_MESSAGE = "invalid_entity.too_short_user_email";
    private static final String TOO_LONG_USER_EMAIL_MESSAGE = "invalid_entity.too_long_user_email";

    private static final String INVALID_PAGE_NUMBER_MESSAGE = "invalid_entity.invalid_page_number";
    private static final String TOO_SMALL_PAGE_NUMBER_MESSAGE = "invalid_entity.too_small_page_number";

    private static final String INVALID_SHOW_DISABLED_PARAM_MESSAGE = "invalid_entity.invalid_show_disabled_param";

    private static final String INVALID_PAGE_SIZE_MESSAGE = "invalid_entity.invalid_page_size";
    private static final String TOO_SMALL_PAGE_SIZE_MESSAGE = "invalid_entity.too_small_page_size";
    private static final String TOO_BIG_PAGE_SIZE_MESSAGE = "invalid_entity.too_big_page_size";

    private static final String PAGE_IS_OUT_OF_RANGE_MESSAGE = "invalid_entity.page_is_out_of_range";

    private static final String REFRESH_TOKEN_EXPIRED_MESSAGE = "refresh_token_expired";
    private static final String INVALID_REFRESH_TOKEN_MESSAGE = "invalid_refresh_token";

    private static final String INVALID_JWT_SIGNATURE_MESSAGE = "invalid_jwt_signature";
    private static final String UNSUPPORTED_JWT_MESSAGE = "unsupported_jwt";
    private static final String JWT_EXPIRED_MESSAGE = "jwt_expired";
    private static final String INVALID_JWT_MESSAGE = "invalid_jwt";

    private static final String INVALID_LOGIN_MESSAGE = "invalid_login";
    private static final String INVALID_PASSWORD_MESSAGE = "invalid_password";

    private static final String INVALID_USER_ORDER_MESSAGE = "access_exception.invalid_order_user";

    private static final String ERROR_SEPARATOR = ", ";

    private static final String APPLICATION_NAME = "api";

    @ExceptionHandler(EntityAlreadyExistsException.class)
    @ResponseStatus(CONFLICT)
    public ResponseEntity<ExceptionResponse> handleEntityAlreadyExists(EntityAlreadyExistsException e) {
        Class<?> entityClass = e.getCauseEntity();
        String entityName = getEntityMessage(entityClass, ENTITY_PLACEHOLDER_MESSAGE);

        String message = "";
        if (TagDto.class.equals(entityClass)) {
            message = " (" + getMessage(TAG_ALREADY_EXISTS_MESSAGE) + ")";
        } else if (GiftCertificateResponseDto.class.equals(entityClass)) {
            message = " (" + getMessage(GIFT_CERTIFICATE_ALREADY_EXISTS_MESSAGE) + ")";
        }

        String errorMessage = String.format(getMessage(ENTITY_ALREADY_EXISTS_MESSAGE), entityName, message);
        return buildErrorResponseEntity(CONFLICT, errorMessage, 40901L);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ExceptionResponse> handleEntityNotAvailable(HttpRequestMethodNotSupportedException e) {
        String errorMessage = getMessage(REQUESTED_METHOD_NOT_AVAILABLE_MESSAGE);
        return buildErrorResponseEntity(BAD_REQUEST, errorMessage, 40014L);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ExceptionResponse> handleAnyException(NoHandlerFoundException e, HttpServletRequest request) {
        String errorMessage = getMessage(NOT_FOUND_MESSAGE);;
        Long errorCode = 40403L;

        String requestURL = e.getRequestURL().replaceFirst("/", "");
        int applicationNameLastPosition = requestURL.indexOf("/");
        String requestedApplicationName = applicationNameLastPosition != -1
                ? requestURL.substring(0, applicationNameLastPosition)
                : "";

        if (requestedApplicationName.equals(APPLICATION_NAME)) {
            requestURL = requestURL.replaceFirst(APPLICATION_NAME + "/", "");
            int apiVersionLastPosition = requestURL.indexOf("/");

            if (apiVersionLastPosition != -1) {
                String apiVersion = requestURL.substring(0, requestURL.indexOf("/"));

                List<String> validApiVersions = Arrays.stream(ApiVersion.values())
                        .map(Enum::name)
                        .map(String::toLowerCase)
                        .collect(Collectors.toList());

                if (!validApiVersions.contains(apiVersion)) {
                    errorMessage = getMessage(INVALID_VERSION_MESSAGE);
                    errorCode = 40404L;
                }
            }
        }
        else {
            errorMessage = getMessage(INVALID_ARTIFACT_MESSAGE);
            errorCode = 40402L;
        }
        return buildErrorResponseEntity(NOT_FOUND, errorMessage, errorCode);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleEntityNotFound(EntityNotFoundException e) {
        Class<?> entityClass = e.getCauseEntity();
        String entityName = getEntityMessage(entityClass, ENTITY_PLACEHOLDER_MESSAGE);

        String errorMessage = String.format(getMessage(ENTITY_NOT_FOUND_MESSAGE), entityName, e.getEntityId());
        return buildErrorResponseEntity(NOT_FOUND, errorMessage, 40401L);
    }

    @ExceptionHandler(EntityNotAvailableException.class)
    public ResponseEntity<ExceptionResponse> handleEntityNotAvailable(EntityNotAvailableException e) {
        Class<?> entityClass = e.getCauseEntity();
        String entityName = getEntityMessage(entityClass, ENTITY_PLACEHOLDER_MESSAGE);

        String errorMessage = String.format(getMessage(ENTITY_NOT_AVAILABLE_MESSAGE), entityName, e.getEntityId());
        return buildErrorResponseEntity(BAD_REQUEST, errorMessage, 40013L);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionResponse> handleAccessDenied(AccessDeniedException e) {
        String errorMessage = getMessage(ACCESS_DENIED_MESSAGE);
        return buildErrorResponseEntity(FORBIDDEN, errorMessage, 40301L);
    }

    @ExceptionHandler(AccessException.class)
    public ResponseEntity<ExceptionResponse> handleAccessDenied(AccessException e) {
        String errorMessage = getMessage(ACCESS_DENIED_MESSAGE);;
        AccessException.State error = e.getState();
        switch (error) {
            case INVALID_ORDER_USER: {
                errorMessage = getMessage(INVALID_USER_ORDER_MESSAGE);
                break;
            }
        }
        return buildErrorResponseEntity(BAD_REQUEST, errorMessage, 400014L);
    }

    @ExceptionHandler(InvalidPaginationException.class)
    public ResponseEntity<ExceptionResponse> handleEntityNotFound(InvalidPaginationException e) {
        List<ValidationError> validationErrors = e.getPaginationErrors();
        StringBuilder errorLine = new StringBuilder();
        for (ValidationError error: validationErrors) {
            switch (error) {
                case INVALID_PAGE_NUMBER: {
                    errorLine.append(getMessage(INVALID_PAGE_NUMBER_MESSAGE));
                    break;
                }
                case TOO_SMALL_PAGE_NUMBER: {
                    errorLine.append(getMessage(TOO_SMALL_PAGE_NUMBER_MESSAGE));
                    break;
                }

                case INVALID_PAGE_SIZE: {
                    errorLine.append(getMessage(INVALID_PAGE_SIZE_MESSAGE));
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
        return buildErrorResponseEntity(BAD_REQUEST, errorMessage, 40001L);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionResponse> handleNotReadable(HttpServletRequest request) {
        String errorMessage;
        long errorCode;
        if (request.getContentLength() == 0) {
            errorMessage = String.format(getMessage(BODY_CANT_BE_EMPTY_MESSAGE), request.getRequestURI());
            errorCode = 40004L;
        }
        else  {
            errorMessage = String.format(getMessage(INVALID_REQUEST_MESSAGE), request.getContextPath());
            errorCode = 40002L;
        }
        return buildErrorResponseEntity(BAD_REQUEST, errorMessage, errorCode);
    }

    @ExceptionHandler(InvalidEntityException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidEntity(InvalidEntityException e) {
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

                case TOO_SHORT_USER_LOGIN: {
                    errorLine.append(getMessage(TOO_SHORT_USER_LOGIN_MESSAGE));
                    break;
                }
                case TOO_LONG_USER_LOGIN: {
                    errorLine.append(getMessage(TOO_LONG_USER_LOGIN_MESSAGE));
                    break;
                }
                case INVALID_USER_LOGIN: {
                    errorLine.append(getMessage(INVALID_USER_LOGIN_MESSAGE));
                    break;
                }
                case NOT_UNIQUE_USER_LOGIN: {
                    errorLine.append(getMessage(NOT_UNIQUE_USER_LOGIN_MESSAGE));
                    break;
                }

                case TOO_SHORT_USER_PASSWORD: {
                    errorLine.append(getMessage(TOO_SHORT_USER_PASSWORD_MESSAGE));
                    break;
                }
                case TOO_LONG_USER_PASSWORD: {
                    errorLine.append(getMessage(TOO_LONG_USER_PASSWORD_MESSAGE));
                    break;
                }
                case INVALID_USER_PASSWORD: {
                    errorLine.append(getMessage(INVALID_USER_PASSWORD_MESSAGE));
                    break;
                }

                case TOO_SHORT_USER_NAME: {
                    errorLine.append(getMessage(TOO_SHORT_USER_NAME_MESSAGE));
                    break;
                }
                case TOO_LONG_USER_NAME: {
                    errorLine.append(getMessage(TOO_LONG_USER_NAME_MESSAGE));
                    break;
                }
                case INVALID_USER_NAME: {
                    errorLine.append(getMessage(INVALID_USER_NAME_MESSAGE));
                    break;
                }

                case TOO_SHORT_USER_SURNAME: {
                    errorLine.append(getMessage(TOO_SHORT_USER_SURNAME_MESSAGE));
                    break;
                }
                case TOO_LONG_USER_SURNAME: {
                    errorLine.append(getMessage(TOO_LONG_USER_SURNAME_MESSAGE));
                    break;
                }
                case INVALID_USER_SURNAME: {
                    errorLine.append(getMessage(INVALID_USER_SURNAME_MESSAGE));
                    break;
                }

                case TOO_SHORT_USER_EMAIL: {
                    errorLine.append(getMessage(TOO_SHORT_USER_EMAIL_MESSAGE));
                    break;
                }
                case TOO_LONG_USER_EMAIL: {
                    errorLine.append(getMessage(TOO_LONG_USER_EMAIL_MESSAGE));
                    break;
                }
                case INVALID_USER_EMAIL: {
                    errorLine.append(getMessage(INVALID_USER_EMAIL_MESSAGE));
                    break;
                }

                case INVALID_TAGS_AMOUNT: {
                    errorLine.append(getMessage(INVALID_TAGS_AMOUNT_MESSAGE));
                    break;
                }
                case NOT_UNIQUE_TAGS_IN_GIFT_CERTIFICATE: {
                    errorLine.append(getMessage(NOT_UNIQUE_TAGS_IN_GIFT_CERTIFICATE_MESSAGE));
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

                case INVALID_SHOW_DISABLED_PARAM: {
                    errorLine.append(getMessage(INVALID_SHOW_DISABLED_PARAM_MESSAGE));
                    break;
                }
            }
            errorLine.append(ERROR_SEPARATOR);
        }
        int lastSeparatorPos = errorLine.length() - ERROR_SEPARATOR.length();
        errorLine.replace(lastSeparatorPos, errorLine.length(), "");

        String errorMessage = String.format(getMessage(INVALID_ENTITY_MESSAGE), errorLine);
        return buildErrorResponseEntity(BAD_REQUEST, errorMessage, 40003L);
    }

    @ExceptionHandler(JwtTokenException.class)
    public ResponseEntity<ExceptionResponse> handleDefault(JwtTokenException e) {
        String errorMessage = getMessage(BAD_REQUEST_MESSAGE);
        long errorCode = 40000L;
        switch (e.getState()) {
            case INVALID_JWT_SIGNATURE: {
                errorMessage = getMessage(INVALID_JWT_SIGNATURE_MESSAGE);
                errorCode = 40007L;
                break;
            }
            case JWT_EXPIRED: {
                errorMessage = getMessage(JWT_EXPIRED_MESSAGE);
                errorCode = 40008L;
                break;
            }
            case UNSUPPORTED_JWT: {
                errorMessage = getMessage(UNSUPPORTED_JWT_MESSAGE);
                errorCode = 40009L;
                break;
            }
            case INVALID_JWT: {
                errorMessage = getMessage(INVALID_JWT_MESSAGE);
                errorCode = 40010L;
                break;
            }
        }
        return buildErrorResponseEntity(BAD_REQUEST, errorMessage, errorCode);
    }

    @ExceptionHandler(RefreshTokenException.class)
    public ResponseEntity<ExceptionResponse> handleRefreshTokenException(RefreshTokenException e) {
        String errorMessage;
        long errorCode;
        if (e.getState().equals(INVALID_TOKEN)) {
            errorMessage = getMessage(INVALID_REFRESH_TOKEN_MESSAGE);
            errorCode = 40005L;
        }
        else  {
            errorMessage = getMessage(REFRESH_TOKEN_EXPIRED_MESSAGE);
            errorCode = 40006L;
        }
        return buildErrorResponseEntity(BAD_REQUEST, errorMessage, errorCode);
    }

    @ExceptionHandler(UserAuthenticationException.class)
    public ResponseEntity<ExceptionResponse> handleUserAuthentication(UserAuthenticationException e) {
        String errorMessage;
        long errorCode;
        if (e.getState().equals(INVALID_LOGIN)) {
            errorMessage = getMessage(INVALID_LOGIN_MESSAGE);
            errorCode = 40011L;
        }
        else  {
            errorMessage = getMessage(INVALID_PASSWORD_MESSAGE);
            errorCode = 40012L;
        }
        return buildErrorResponseEntity(BAD_REQUEST, errorMessage, errorCode);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleDefault(Exception e) {
        logger.error("Exception appeared: ", e);
        String errorMessage = getMessage(INTERNAL_SERVER_ERROR_MESSAGE);
        return buildErrorResponseEntity(INTERNAL_SERVER_ERROR, errorMessage, 50001L);
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
        else if (GiftCertificateResponseDto.class.equals(entity)) {
            entityName = getMessage(GIFT_CERTIFICATE_MESSAGE);
        }
        else if (GiftCertificateFilterDto.class.equals(entity)) {
            entityName = getMessage(GIFT_CERTIFICATE_FILTER_MESSAGE);
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
        else if (TokenDto.class.equals(entity)) {
            entityName = getMessage(TOKEN_MESSAGE);
        }
        else if (UserSignInDto.class.equals(entity)) {
            entityName = getMessage(USER_SIGN_IN_MESSAGE);
        }
        else if (UserSignUpDto.class.equals(entity)) {
            entityName = getMessage(USER_SIGN_UP_MESSAGE);
        }
        else if (UserOrderResponseDto.class.equals(entity)) {
            entityName = getMessage(USER_ORDER_RESPONSE_MESSAGE);
        }
        return entityName;
    }

    private ResponseEntity<ExceptionResponse> buildErrorResponseEntity(HttpStatus status, String errorMessage, Long errorCode) {
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .errorMessage(errorMessage)
                .errorCode(errorCode)
                .build();
        return new ResponseEntity<>(exceptionResponse, status);
    }
}
