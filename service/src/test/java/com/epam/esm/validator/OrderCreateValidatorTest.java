package com.epam.esm.validator;

import com.epam.esm.dto.OrderCreateRequestDto;
import com.epam.esm.dto.OrderItemDto;
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
import static com.epam.esm.validator.ValidationError.ORDER_USER_REQUIRED;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderCreateValidatorTest {
    private OrderCreateValidator orderCreateValidator = new OrderCreateValidator();

    @ParameterizedTest
    @MethodSource("provideOrderCreateParams")
    void testOrderCreateValidator(List<OrderItemDto> orderItemsDto, List<ValidationError> expected) {

        List<ValidationError> actual = orderCreateValidator.validateParams(orderItemsDto);

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("provideRequiredOrderCreateParams")
    void testOrderCreateValidator(OrderCreateRequestDto orderCreateRequestDto, List<ValidationError> expected) {

        List<ValidationError> actual = orderCreateValidator.validateWithRequiredParams(orderCreateRequestDto);

        assertEquals(expected, actual);
    }

    private static List<Arguments> provideOrderCreateParams() {
        List<Arguments> testCases = new ArrayList<>();

        testCases.add(Arguments.of(generateOrderItems(10), Collections.emptyList()));
        testCases.add(Arguments.of(generateOrderItems(500), Collections.singletonList(INVALID_ORDER_ITEMS_AMOUNT)));
        List<OrderItemDto> notUniqueItems = generateOrderItems(10);
        notUniqueItems.addAll(generateOrderItems(10));
        testCases.add(Arguments.of(notUniqueItems, Collections.singletonList(NOT_UNIQUE_GIFT_CERTIFICATES_IN_ORDER)));

        return testCases;
    }

    private static List<Arguments> provideRequiredOrderCreateParams() {
        List<Arguments> testCases = new ArrayList<>();

        testCases.add(Arguments.of(OrderCreateRequestDto.builder()
                .userId(1)
                .orderGiftCertificates(generateOrderItems(10))
                .build(),
                Collections.emptyList()));
        testCases.add(Arguments.of(OrderCreateRequestDto.builder()
                        .userId(0L)
                        .orderGiftCertificates(generateOrderItems(10))
                        .build(),
                Collections.singletonList(ORDER_USER_REQUIRED)));
        testCases.add(Arguments.of(OrderCreateRequestDto.builder()
                        .userId(1)
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
