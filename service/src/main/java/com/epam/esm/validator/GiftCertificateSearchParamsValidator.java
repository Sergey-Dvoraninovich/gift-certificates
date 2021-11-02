package com.epam.esm.validator;

import com.epam.esm.repository.OrderingType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static com.epam.esm.validator.ValidationError.*;

@Component
public class GiftCertificateSearchParamsValidator {
    private static final String TAG_NAME_REGEXP = "^[A-Za-z]{2,45}$";
    private static final int TAG_NAME_MIN_LENGTH = 2;
    private static final int TAG_NAME_MAX_LENGTH = 45;

    private static final String GIFT_CERTIFICATE_NAME_REGEXP = "^[A-Za-z]{2,45}$";
    private static final int GIFT_CERTIFICATE_NAME_MIN_LENGTH = 2;
    private static final int GIFT_CERTIFICATE_NAME_MAX_LENGTH = 45;

    private static final String DESCRIPTION_REGEXP = "^[A-Za-z,.:;!?\\s]{2,500}$";
    private static final int DESCRIPTION_MIN_LENGTH = 2;
    private static final int DESCRIPTION_MAX_LENGTH = 500;

    public List<ValidationError> validate(String tagName, String certificateName, String orderingName,
                                          String certificateDescription, String orderingCreateDate) {
        List<ValidationError> validationErrors = new ArrayList<>();

        if (tagName != null) {
            if (tagName.length() < TAG_NAME_MIN_LENGTH) {
                validationErrors.add(TOO_SHORT_TAG_NAME);
            }
            else if (tagName.length() > TAG_NAME_MAX_LENGTH) {
                validationErrors.add(TOO_LONG_TAG_NAME);
            }
            else if (!Pattern.matches(TAG_NAME_REGEXP, tagName)) {
                validationErrors.add(INVALID_SYMBOLS_IN_TAG_NAME);
            }
        }

        if (certificateName != null) {
            if (certificateName.length() < GIFT_CERTIFICATE_NAME_MIN_LENGTH) {
                validationErrors.add(TOO_SHORT_GIFT_CERTIFICATE_NAME);
            }
            else if (certificateName.length() > GIFT_CERTIFICATE_NAME_MAX_LENGTH) {
                validationErrors.add(TOO_LONG_GIFT_CERTIFICATE_NAME);
            }
            else if (!Pattern.matches(GIFT_CERTIFICATE_NAME_REGEXP, certificateName)) {
                validationErrors.add(INVALID_SYMBOLS_IN_GIFT_CERTIFICATE_NAME);
            }
        }

        if (certificateDescription != null) {
            if (certificateDescription.length() < DESCRIPTION_MIN_LENGTH) {
                validationErrors.add(TOO_SHORT_DESCRIPTION);
            }
            else if (certificateDescription.length() > DESCRIPTION_MAX_LENGTH) {
                validationErrors.add(TOO_LONG_DESCRIPTION);
            }
            else if (!Pattern.matches(DESCRIPTION_REGEXP, certificateDescription)) {
                validationErrors.add(INVALID_SYMBOLS_IN_DESCRIPTION);
            }
        }

        if (orderingName != null) {
            List<OrderingType> orderingTypes = Arrays.asList(OrderingType.values());
            boolean result = orderingTypes.stream().filter(type -> type.name().equals(orderingName)).count() == 1;
            if (!result) {
                validationErrors.add(INVALID_NAME_ORDERING_TYPE);
            }
        }

        if (orderingCreateDate != null) {
            List<OrderingType> orderingTypes = Arrays.asList(OrderingType.values());
            boolean result = orderingTypes.stream().filter(type -> type.name().equals(orderingCreateDate)).count() == 1;
            if (!result) {
                validationErrors.add(INVALID_CREATE_DATE_ORDERING_TYPE);
            }
        }

        return validationErrors;
    }
}
