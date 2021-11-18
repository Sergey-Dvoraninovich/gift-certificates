package com.epam.esm.validator;

import com.epam.esm.dto.OrderItemDto;
import com.epam.esm.dto.OrderUpdateRequestDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static com.epam.esm.validator.ValidationError.*;
import static com.epam.esm.validator.ValidationError.NOT_UNIQUE_GIFT_CERTIFICATES_IN_ORDER;

@Component
public class OrderUpdateValidator {
    public List<ValidationError> validateWithRequiredParams(OrderUpdateRequestDto orderUpdateRequestDto) {
        List<ValidationError> validationErrors = new ArrayList<>();

        if (orderUpdateRequestDto.getOrderGiftCertificates() == null){
            validationErrors.add(ORDER_ITEMS_REQUIRED);
        }
        if (validationErrors.size() == 0) {
            validationErrors.addAll(validateParams(orderUpdateRequestDto.getOrderGiftCertificates()));
        }
        return validationErrors;
    }

    public List<ValidationError> validateParams(List<OrderItemDto> orderItemsDto) {
        List<ValidationError> validationErrors = new ArrayList<>();

        if (orderItemsDto != null) {
            if (orderItemsDto.size() == 0){
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
