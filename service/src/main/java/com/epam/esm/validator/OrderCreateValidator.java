package com.epam.esm.validator;

import com.epam.esm.dto.OrderCreateRequestDto;
import com.epam.esm.dto.OrderItemDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static com.epam.esm.validator.ValidationError.*;

@Component
public class OrderCreateValidator {
    public List<ValidationError> validateWithRequiredParams(OrderCreateRequestDto orderCreateRequestDto) {
        List<ValidationError> validationErrors = new ArrayList<>();

        if (orderCreateRequestDto.getUser() == null){
            validationErrors.add(ORDER_USER_REQUIRED);
        }
        if (orderCreateRequestDto.getOrderGiftCertificates() == null){
            validationErrors.add(ORDER_ITEMS_REQUIRED);
        }
        if (validationErrors.size() == 0) {
            validationErrors.addAll(validateParams(orderCreateRequestDto.getOrderGiftCertificates()));
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
