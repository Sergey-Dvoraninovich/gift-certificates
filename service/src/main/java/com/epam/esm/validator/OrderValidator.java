package com.epam.esm.validator;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.OrderItemDto;
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
        if (orderDto.getOrderItems() == null){
            validationErrors.add(ORDER_ITEMS_REQUIRED);
        }
        if (validationErrors.size() == 0) {
            validationErrors.addAll(validateParams(orderDto.getOrderItems()));
        }
        return validationErrors;
    }

    public List<ValidationError> validateParams(List<OrderItemDto> orderItemsDto) {
        List<ValidationError> validationErrors = new ArrayList<>();

        if (orderItemsDto != null) {
            if (orderItemsDto.size() == 0){
                validationErrors.add(INVALID_ORDER_ITEMS_AMOUNT);
            }
        }

        return validationErrors;
    }
}
