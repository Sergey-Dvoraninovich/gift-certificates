package com.epam.esm.validator;

public enum ValidationError {
    NAME_REQUIRED,
    DESCRIPTION_REQUIRED,
    PRICE_REQUIRED,
    DURATION_REQUIRED,

    TOO_SHORT_NAME,
    TOO_LONG_NAME,
    INVALID_SYMBOLS_IN_NAME,
    INVALID_LEADING_OR_CLOSING_SYMBOLS_IN_NAME,

    TOO_SHORT_DESCRIPTION,
    TOO_LONG_DESCRIPTION,
    INVALID_SYMBOLS_IN_DESCRIPTION,
    INVALID_LEADING_OR_CLOSING_SYMBOLS_IN_DESCRIPTION,

    TOO_SHORT_DURATION,
    TOO_LONG_DURATION,
    INVALID_SYMBOLS_IN_DURATION,

    TOO_SMALL_PRICE,
    TOO_BIG_PRICE,
    INVALID_PRICE_FORMAT,

    TOO_SHORT_TAG_NAME,
    TOO_LONG_TAG_NAME,
    INVALID_SYMBOLS_IN_TAG_NAME,

    TOO_SHORT_GIFT_CERTIFICATE_NAME,
    TOO_LONG_GIFT_CERTIFICATE_NAME,
    INVALID_SYMBOLS_IN_GIFT_CERTIFICATE_NAME,

    INVALID_NAME_ORDERING_TYPE,
    INVALID_CREATE_DATE_ORDERING_TYPE
}
