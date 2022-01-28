package com.epam.esm.validator;

import com.epam.esm.repository.OrderingType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.epam.esm.validator.ValidationError.INVALID_ORDER_ORDERING_TYPE;

@Component
public class OrderSearchParamsValidator {
    public List<ValidationError> validate(String sortOrder) {
        List<ValidationError> validationErrors = new ArrayList<>();

        if (sortOrder != null) {
            List<OrderingType> orderingTypes = Arrays.asList(OrderingType.values());
            boolean result = orderingTypes.stream().filter(type -> type.name().equals(sortOrder)).count() == 1;
            if (!result) {
                validationErrors.add(INVALID_ORDER_ORDERING_TYPE);
            }
        }

        return validationErrors;
    }
}
