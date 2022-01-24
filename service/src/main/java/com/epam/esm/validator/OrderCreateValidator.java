package com.epam.esm.validator;

import com.epam.esm.dto.OrderCreateRequestDto;
import com.epam.esm.dto.OrderItemDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static com.epam.esm.validator.ValidationError.INVALID_ORDER_ITEMS_AMOUNT;
import static com.epam.esm.validator.ValidationError.NOT_UNIQUE_GIFT_CERTIFICATES_IN_ORDER;
import static com.epam.esm.validator.ValidationError.ORDER_ITEMS_REQUIRED;
import static com.epam.esm.validator.ValidationError.ORDER_USER_REQUIRED;

@Component
public class OrderCreateValidator {
    private static final Long MAX_ORDER_ITEMS_AMOUNT = 100L;

    public List<ValidationError> validateWithRequiredParams(OrderCreateRequestDto orderCreateRequestDto) {
        List<ValidationError> validationErrors = new ArrayList<>();

        if (orderCreateRequestDto.getUserId() == 0){
            validationErrors.add(ORDER_USER_REQUIRED);
        }
        if (orderCreateRequestDto.getOrderGiftCertificates() == null){
            validationErrors.add(ORDER_ITEMS_REQUIRED);
        }
        if (validationErrors.isEmpty()) {
            validationErrors.addAll(validateParams(orderCreateRequestDto.getOrderGiftCertificates()));
        }
        return validationErrors;
    }

    public List<ValidationError> validateParams(List<OrderItemDto> orderItemsDto) {
        List<ValidationError> validationErrors = new ArrayList<>();

        if (orderItemsDto != null) {
            if (orderItemsDto.size() > MAX_ORDER_ITEMS_AMOUNT){
                validationErrors.add(INVALID_ORDER_ITEMS_AMOUNT);
            }
            HashSet<Long> giftCertificateIds = new HashSet<>();
            for (OrderItemDto orderItemDto: orderItemsDto) {
                giftCertificateIds.add(orderItemDto.getId());
            }
            if (giftCertificateIds.size() != orderItemsDto.size()) {
                validationErrors.add(NOT_UNIQUE_GIFT_CERTIFICATES_IN_ORDER);
            }
        }

        return validationErrors;
    }
}
