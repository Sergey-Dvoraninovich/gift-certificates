package com.epam.esm.validator;

import com.epam.esm.dto.OrderItemDto;
import com.epam.esm.dto.OrderUpdateRequestDto;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.epam.esm.validator.ValidationError.INVALID_ORDER_ITEMS_AMOUNT;
import static com.epam.esm.validator.ValidationError.NOT_UNIQUE_GIFT_CERTIFICATES_IN_ORDER;
import static com.epam.esm.validator.ValidationError.ORDER_ITEMS_REQUIRED;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderUpdateValidatorTest {
    private OrderUpdateValidator orderUpdateValidator = new OrderUpdateValidator();

    @ParameterizedTest
    @MethodSource("provideOrderUpdateParams")
    void testOrderCreateValidator(List<OrderItemDto> orderItemsDto, List<ValidationError> expected) {

        List<ValidationError> actual = orderUpdateValidator.validateParams(orderItemsDto);

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("provideRequiredOrderUpdateParams")
    void testOrderCreateValidator(OrderUpdateRequestDto orderUpdateRequestDto, List<ValidationError> expected) {

        List<ValidationError> actual = orderUpdateValidator.validateWithRequiredParams(orderUpdateRequestDto);

        assertEquals(expected, actual);
    }

    private static List<Arguments> provideOrderUpdateParams() {
        List<Arguments> testCases = new ArrayList<>();

        testCases.add(Arguments.of(generateOrderItems(10), Collections.emptyList()));
        testCases.add(Arguments.of(generateOrderItems(500), Collections.singletonList(INVALID_ORDER_ITEMS_AMOUNT)));
        List<OrderItemDto> notUniqueItems = generateOrderItems(10);
        notUniqueItems.addAll(generateOrderItems(10));
        testCases.add(Arguments.of(notUniqueItems, Collections.singletonList(NOT_UNIQUE_GIFT_CERTIFICATES_IN_ORDER)));

        return testCases;
    }

    private static List<Arguments> provideRequiredOrderUpdateParams() {
        List<Arguments> testCases = new ArrayList<>();

        testCases.add(Arguments.of(OrderUpdateRequestDto.builder()
                        .orderGiftCertificates(generateOrderItems(10))
                        .build(),
                Collections.emptyList()));
        testCases.add(Arguments.of(OrderUpdateRequestDto.builder()
                        .orderGiftCertificates(null)
                        .build(),
                Collections.singletonList(ORDER_ITEMS_REQUIRED)));

        return testCases;
    }

    private static List<OrderItemDto> generateOrderItems(int amount){
        List<OrderItemDto> result = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            result.add(OrderItemDto.builder()
                    .id(i)
                    .price(new BigDecimal("50.00"))
                    .build());
        }
        return result;
    }
}
