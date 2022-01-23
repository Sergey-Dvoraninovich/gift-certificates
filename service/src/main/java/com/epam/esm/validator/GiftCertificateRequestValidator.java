package com.epam.esm.validator;

import com.epam.esm.dto.GiftCertificateRequestDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

import static com.epam.esm.validator.ValidationError.GIFT_CERTIFICATE_DESCRIPTION_REQUIRED;
import static com.epam.esm.validator.ValidationError.GIFT_CERTIFICATE_DURATION_REQUIRED;
import static com.epam.esm.validator.ValidationError.GIFT_CERTIFICATE_NAME_REQUIRED;
import static com.epam.esm.validator.ValidationError.GIFT_CERTIFICATE_PRICE_REQUIRED;
import static com.epam.esm.validator.ValidationError.INVALID_GIFT_CERTIFICATE_PRICE_FORMAT;
import static com.epam.esm.validator.ValidationError.INVALID_LEADING_OR_CLOSING_SYMBOLS_IN_GIFT_CERTIFICATE_DESCRIPTION;
import static com.epam.esm.validator.ValidationError.INVALID_LEADING_OR_CLOSING_SYMBOLS_IN_GIFT_CERTIFICATE_NAME;
import static com.epam.esm.validator.ValidationError.INVALID_SYMBOLS_IN_GIFT_CERTIFICATE_DESCRIPTION;
import static com.epam.esm.validator.ValidationError.INVALID_SYMBOLS_IN_GIFT_CERTIFICATE_DURATION;
import static com.epam.esm.validator.ValidationError.INVALID_SYMBOLS_IN_GIFT_CERTIFICATE_NAME;
import static com.epam.esm.validator.ValidationError.INVALID_TAGS_AMOUNT;
import static com.epam.esm.validator.ValidationError.NOT_UNIQUE_TAGS_IN_GIFT_CERTIFICATE;
import static com.epam.esm.validator.ValidationError.TOO_BIG_GIFT_CERTIFICATE_PRICE;
import static com.epam.esm.validator.ValidationError.TOO_LONG_GIFT_CERTIFICATE_DESCRIPTION;
import static com.epam.esm.validator.ValidationError.TOO_LONG_GIFT_CERTIFICATE_DURATION;
import static com.epam.esm.validator.ValidationError.TOO_LONG_GIFT_CERTIFICATE_NAME;
import static com.epam.esm.validator.ValidationError.TOO_SHORT_GIFT_CERTIFICATE_DESCRIPTION;
import static com.epam.esm.validator.ValidationError.TOO_SHORT_GIFT_CERTIFICATE_DURATION;
import static com.epam.esm.validator.ValidationError.TOO_SHORT_GIFT_CERTIFICATE_NAME;
import static com.epam.esm.validator.ValidationError.TOO_SMALL_GIFT_CERTIFICATE_PRICE;

@Component
public class GiftCertificateRequestValidator {
    private static final String NAME_NO_LEADING_SYMBOLS_REGEXP = "^[A-Za-z]{1}[A-Za-z\\s]{0,498}[A-Za-z]{1}$";
    private static final String NAME_SYMBOLS_REGEXP = "^[A-Za-z\\s]{2,45}$";
    private static final int NAME_MIN_LENGTH = 2;
    private static final int NAME_MAX_LENGTH = 45;

    private static final String DESCRIPTION_NO_LEADING_SYMBOLS_REGEXP = "^[A-Za-z]{1}[A-Za-z,.:;!?\\s]{0,498}[A-Za-z,.:;!?]{1}$";
    private static final String DESCRIPTION_SYMBOLS_REGEXP = "^[A-Za-z,.:;!?\\s]{2,500}$";
    private static final int DESCRIPTION_MIN_LENGTH = 2;
    private static final int DESCRIPTION_MAX_LENGTH = 500;

    private static final String DURATION_REGEXP = "^\\d{1,4}$";
    private static final String DURATION_VALID_UNLIMITED_REGEXP = "^\\d{5,100}$";
    private static final Duration MIN_DURATION = Duration.ofDays(28);
    private static final Duration MAX_DURATION = Duration.ofDays(1096);

    private static final String PRICE_REGEXP = "^([0-9]{1,5}([,.][0-9]{0,2})?|[,.][0-9]{1,2})$";
    private static final BigDecimal MIN_PRICE = new BigDecimal("9.99");
    private static final BigDecimal MAX_PRICE = new BigDecimal("10000");

    private static final int MAX_TAGS_AMOUNT = 100;

    public List<ValidationError> validateWithRequiredParams(GiftCertificateRequestDto certificateDto) {
        List<ValidationError> validationErrors = new ArrayList<>();
        if (certificateDto.getName() == null){
            validationErrors.add(GIFT_CERTIFICATE_NAME_REQUIRED);
        }
        if (certificateDto.getDescription() == null){
            validationErrors.add(GIFT_CERTIFICATE_DESCRIPTION_REQUIRED);
        }
        if (certificateDto.getPrice() == null){
            validationErrors.add(GIFT_CERTIFICATE_PRICE_REQUIRED);
        }
        if (certificateDto.getDuration() == null){
            validationErrors.add(GIFT_CERTIFICATE_DURATION_REQUIRED);
        }
        if (validationErrors.isEmpty()) {
            validationErrors.addAll(validateParams(certificateDto.getName(), certificateDto.getDescription(), certificateDto.getPrice().toString(),
                    String.valueOf(certificateDto.getDuration().toDays()), certificateDto.getTagIdsDto()));
        }
        return validationErrors;
    }

    public List<ValidationError> validateParams(String name, String description, String price, String duration, List<Long> tagIdsDto) {
        List<ValidationError> validationErrors = new ArrayList<>();

        if (name != null) {
            if (name.length() < NAME_MIN_LENGTH) {
                validationErrors.add(TOO_SHORT_GIFT_CERTIFICATE_NAME);
            }
            else if (name.length() > NAME_MAX_LENGTH) {
                validationErrors.add(TOO_LONG_GIFT_CERTIFICATE_NAME);
            }
            else {
                if (!Pattern.matches(NAME_NO_LEADING_SYMBOLS_REGEXP, name)
                        && Pattern.matches(NAME_SYMBOLS_REGEXP, name)) {
                    validationErrors.add(INVALID_LEADING_OR_CLOSING_SYMBOLS_IN_GIFT_CERTIFICATE_NAME);
                }
                if (!Pattern.matches(NAME_SYMBOLS_REGEXP, name)) {
                    validationErrors.add(INVALID_SYMBOLS_IN_GIFT_CERTIFICATE_NAME);
                }
            }
        }

        if (description != null) {
            if (description.length() < DESCRIPTION_MIN_LENGTH) {
                validationErrors.add(TOO_SHORT_GIFT_CERTIFICATE_DESCRIPTION);
            }
            else if (description.length() > DESCRIPTION_MAX_LENGTH) {
                validationErrors.add(TOO_LONG_GIFT_CERTIFICATE_DESCRIPTION);
            }
            else {
                if (!Pattern.matches(DESCRIPTION_NO_LEADING_SYMBOLS_REGEXP, description)
                    && Pattern.matches(DESCRIPTION_SYMBOLS_REGEXP, description)) {
                    validationErrors.add(INVALID_LEADING_OR_CLOSING_SYMBOLS_IN_GIFT_CERTIFICATE_DESCRIPTION);
                }
                if (!Pattern.matches(DESCRIPTION_SYMBOLS_REGEXP, description)) {
                    validationErrors.add(INVALID_SYMBOLS_IN_GIFT_CERTIFICATE_DESCRIPTION);
                }
            }
        }

        if (duration != null) {
            if (!Pattern.matches(DURATION_REGEXP, duration)) {
                if (Pattern.matches(DURATION_VALID_UNLIMITED_REGEXP, duration)) {
                    validationErrors.add(TOO_LONG_GIFT_CERTIFICATE_DURATION);
                }
                else {
                    validationErrors.add(INVALID_SYMBOLS_IN_GIFT_CERTIFICATE_DURATION);
                }
            } else {
                Duration durationValue = Duration.ofDays(Long.parseLong(duration));
                if (durationValue.compareTo(MIN_DURATION) < 0) {
                     validationErrors.add(TOO_SHORT_GIFT_CERTIFICATE_DURATION);
                }
                if (MAX_DURATION.compareTo(durationValue) < 0) {
                     validationErrors.add(TOO_LONG_GIFT_CERTIFICATE_DURATION);
                }
            }
        }

        if (price != null) {
            if (!Pattern.matches(PRICE_REGEXP, price)) {
                validationErrors.add(INVALID_GIFT_CERTIFICATE_PRICE_FORMAT);
            } else {
                BigDecimal priceValue = new BigDecimal(price);
                if (priceValue.compareTo(MIN_PRICE) < 0) {
                    validationErrors.add(TOO_SMALL_GIFT_CERTIFICATE_PRICE);
                }
                if (MAX_PRICE.compareTo(priceValue) < 0) {
                    validationErrors.add(TOO_BIG_GIFT_CERTIFICATE_PRICE);
                }
            }
        }

        if (tagIdsDto != null) {
            if (tagIdsDto.size() > MAX_TAGS_AMOUNT){
                validationErrors.add(INVALID_TAGS_AMOUNT);
            }
            HashSet<Long> tagIds = new HashSet<>();
            for (Long id: tagIdsDto) {
                tagIds.add(id);
            }
            if (tagIdsDto.size() != tagIds.size()) {
                validationErrors.add(NOT_UNIQUE_TAGS_IN_GIFT_CERTIFICATE);
            }
        }

        return validationErrors;
    }
}
