package com.epam.esm.validator;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.OrderDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.epam.esm.validator.ValidationError.*;

@Component
public class OrderValidator {
    public List<ValidationError> validateWithRequiredParams(OrderDto orderDto) {
        List<ValidationError> validationErrors = new ArrayList<>();

        if (orderDto.getUser() == null){
            validationErrors.add(ORDER_USER_REQUIRED);
        }
        if (orderDto.getOrderGiftCertificates() == null){
            validationErrors.add(ORDER_GIFT_CERTIFICATES_REQUIRED);
        }
        if (validationErrors.size() == 0) {
            validationErrors.addAll(validateParams(orderDto.getOrderGiftCertificates()));
        }
        return validationErrors;
    }

    public List<ValidationError> validateParams(List<GiftCertificateDto> certificatesDto) {
        List<ValidationError> validationErrors = new ArrayList<>();

        if (certificatesDto != null) {
            if (certificatesDto.size() == 0){
                validationErrors.add(INVALID_ORDER_GIFT_CERTIFICATES_AMOUNT);
            }
        }

        return validationErrors;
    }
}
