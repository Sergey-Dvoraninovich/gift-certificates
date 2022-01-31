package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateFilterDto;
import com.epam.esm.dto.OrderFilterDto;
import com.epam.esm.dto.PageDto;
import com.epam.esm.exception.InvalidEntityException;
import com.epam.esm.exception.InvalidPaginationException;
import com.epam.esm.service.impl.RequestServiceImpl;
import com.epam.esm.validator.GiftCertificateSearchParamsValidator;
import com.epam.esm.validator.OrderSearchParamsValidator;
import com.epam.esm.validator.PaginationValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.epam.esm.repository.OrderingType.ASC;
import static com.epam.esm.repository.OrderingType.DESC;
import static com.epam.esm.validator.ValidationError.INVALID_NAME_ORDERING_TYPE;
import static com.epam.esm.validator.ValidationError.INVALID_ORDER_ORDERING_TYPE;
import static com.epam.esm.validator.ValidationError.INVALID_PAGE_NUMBER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestServiceTest {
    private static final String DELIMITER = ",";

    private static final String NAME = "name";
    private static final String SHOW_DISABLED = "showDisabled";
    private static final String DESCRIPTION = "description";
    private static final String TAG_NAMES = "tagNames";
    private static final String ORDERING_NAME = "orderingName";
    private static final String ORDERING_CREATE_DATE = "orderingCreateDate";

    private static final String ORDERING_CREATE_TIME = "orderingCreateTime";

    private static final String PAGE_NUMBER = "pageNumber";
    private static final String PAGE_SIZE = "pageSize";
    private static final int DEFAULT_PAGE_SIZE = 10;

    @InjectMocks
    private RequestServiceImpl requestService;

    @Mock
    private PaginationValidator paginationValidator;

    @Mock
    private OrderSearchParamsValidator orderSearchParamsValidator;

    @Mock
    private GiftCertificateSearchParamsValidator giftCertificateSearchParamsValidator;

    @ParameterizedTest
    @MethodSource("providePageDtoParams")
    void testCreatePageDto(Map<String, Object> params, Long totalAmount, PageDto expected) {
        when(paginationValidator.validateParams((String) params.get(PAGE_NUMBER),
                (String) params.get(PAGE_SIZE))).thenReturn(Collections.emptyList());

        PageDto actual = requestService.createPageDTO(params, totalAmount);

        assertEquals(expected, actual);
    }

    @Test
    void testCreatePageDtoInvalidParams() {
        Map<String, Object> params = new HashMap<>();
        params.put(PAGE_NUMBER, "12%");
        params.put(PAGE_SIZE, "10");
        when(paginationValidator.validateParams((String) params.get(PAGE_NUMBER),
                (String) params.get(PAGE_SIZE))).thenReturn(List.of(INVALID_PAGE_NUMBER));

        try {
            requestService.createPageDTO(params, 550L);
            fail();
        } catch (InvalidPaginationException e) {
            assertTrue(true);
        }
    }

    @Test
    void testCreatePageDtoInvalidPageSize() {
        Map<String, Object> params = new HashMap<>();
        params.put(PAGE_NUMBER, "12");
        params.put(PAGE_SIZE, "10");
        when(paginationValidator.validateParams((String) params.get(PAGE_NUMBER),
                (String) params.get(PAGE_SIZE))).thenReturn(Collections.emptyList());

        try {
            requestService.createPageDTO(params, 110L);
            fail();
        } catch (InvalidPaginationException e) {
            assertTrue(true);
        }
    }

    @ParameterizedTest
    @MethodSource("provideOrderFilterDtoParams")
    void testCreateOrderFilterDto(Map<String, Object> params, OrderFilterDto expected) {
        when(orderSearchParamsValidator.validate((String) params.get(ORDERING_CREATE_TIME)))
                .thenReturn(Collections.emptyList());

        OrderFilterDto actual = requestService.createOrderFilterDto(params);

        assertEquals(expected, actual);
    }

    @Test
    void testCreateOrderFilterDtoInvalidParams() {
        Map<String, Object> params = new HashMap<>();
        params.put(ORDERING_CREATE_TIME, "text");
        when(orderSearchParamsValidator.validate((String) params.get(ORDERING_CREATE_TIME)))
                .thenReturn(List.of(INVALID_ORDER_ORDERING_TYPE));

        try {
            requestService.createOrderFilterDto(params);
            fail();
        } catch (InvalidEntityException e) {
            assertTrue(true);
        }
    }

    @ParameterizedTest
    @MethodSource("provideGiftCertificateFilterDtoParams")
    void testCreateGiftCertificateFilterDto(Map<String, Object> params, GiftCertificateFilterDto expected) {
        String tagNamesLine = (String) params.get(TAG_NAMES);
        List<String> tagNames = tagNamesLine == null ? null : List.of(tagNamesLine.split(DELIMITER));
        when(giftCertificateSearchParamsValidator.validate(tagNames, (String) params.get(NAME),
                (String) params.get(ORDERING_NAME), (String) params.get(DESCRIPTION),
                (String) params.get(ORDERING_CREATE_DATE), (String) params.get(SHOW_DISABLED)))
                .thenReturn(Collections.emptyList());

        GiftCertificateFilterDto actual = requestService.createGiftCertificateFilterDTO(params);

        assertEquals(expected, actual);
    }

    @Test
    void testCreateGiftCertificateFilterDtoInvalidParams() {
        Map<String, Object> params = new HashMap<>();
        params.put(ORDERING_NAME, "asc!");
        when(giftCertificateSearchParamsValidator.validate(null, (String) params.get(NAME),
                (String) params.get(ORDERING_NAME), (String) params.get(DESCRIPTION),
                (String) params.get(ORDERING_CREATE_DATE), (String) params.get(SHOW_DISABLED)))
                .thenReturn(List.of(INVALID_NAME_ORDERING_TYPE));

        try {
            requestService.createGiftCertificateFilterDTO(params);
            fail();
        } catch (InvalidEntityException e) {
            assertTrue(true);
        }
    }

    private static List<Arguments> providePageDtoParams() {
        List<Arguments> testCases = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        PageDto page;

        params.put(PAGE_NUMBER, "1");
        params.put(PAGE_SIZE, "10");
        page = PageDto.builder()
                .pageNumber(1)
                .pageSize(10)
                .build();
        testCases.add(Arguments.of(params, 20L, page));

        params = new HashMap<>();
        params.put(PAGE_NUMBER, "3");
        params.put(PAGE_SIZE, "20");
        page = PageDto.builder()
                .pageNumber(3)
                .pageSize(20)
                .build();
        testCases.add(Arguments.of(params, 50L, page));

        params = new HashMap<>();
        params.put(PAGE_NUMBER, null);
        params.put(PAGE_SIZE, null);
        page = PageDto.builder()
                .pageNumber(1)
                .pageSize(DEFAULT_PAGE_SIZE)
                .build();
        testCases.add(Arguments.of(params, 20L, page));

        params = new HashMap<>();
        page = PageDto.builder()
                .pageNumber(1)
                .pageSize(10)
                .build();
        testCases.add(Arguments.of(params, 20L, page));

        return testCases;
    }

    private static List<Arguments> provideOrderFilterDtoParams() {
        List<Arguments> testCases = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        OrderFilterDto orderFilterDto;

        params.put(ORDERING_CREATE_TIME, ASC.name());
        orderFilterDto = OrderFilterDto.builder()
                .orderingCreateTime(ASC)
                .build();
        testCases.add(Arguments.of(params, orderFilterDto));

        params = new HashMap<>();
        params.put(ORDERING_CREATE_TIME, null);
        orderFilterDto = OrderFilterDto.builder()
                .orderingCreateTime(null)
                .build();
        testCases.add(Arguments.of(params, orderFilterDto));

        return testCases;
    }

    private static List<Arguments> provideGiftCertificateFilterDtoParams() {
        List<Arguments> testCases = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        GiftCertificateFilterDto giftCertificateFilterDto;

        giftCertificateFilterDto = GiftCertificateFilterDto.builder()
                .showDisabled(false)
                .build();
        testCases.add(Arguments.of(params, giftCertificateFilterDto));

        params = new HashMap<>();
        params.put(NAME, "certificate");
        giftCertificateFilterDto = GiftCertificateFilterDto.builder()
                .name("certificate")
                .showDisabled(false)
                .build();
        testCases.add(Arguments.of(params, giftCertificateFilterDto));

        params = new HashMap<>();
        params.put(SHOW_DISABLED, "true");
        giftCertificateFilterDto = GiftCertificateFilterDto.builder()
                .showDisabled(true)
                .build();
        testCases.add(Arguments.of(params, giftCertificateFilterDto));

        params = new HashMap<>();
        params.put(DESCRIPTION, "certificate");
        giftCertificateFilterDto = GiftCertificateFilterDto.builder()
                .showDisabled(false)
                .description("certificate")
                .build();
        testCases.add(Arguments.of(params, giftCertificateFilterDto));

        params = new HashMap<>();
        params.put(TAG_NAMES, "one,two");
        giftCertificateFilterDto = GiftCertificateFilterDto.builder()
                .showDisabled(false)
                .tagNames(List.of("one", "two"))
                .build();
        testCases.add(Arguments.of(params, giftCertificateFilterDto));

        params = new HashMap<>();
        params.put(ORDERING_NAME, "ASC");
        giftCertificateFilterDto = GiftCertificateFilterDto.builder()
                .orderingName(ASC)
                .showDisabled(false)
                .build();
        testCases.add(Arguments.of(params, giftCertificateFilterDto));

        params = new HashMap<>();
        params.put(ORDERING_CREATE_DATE, "DESC");
        giftCertificateFilterDto = GiftCertificateFilterDto.builder()
                .showDisabled(false)
                .orderingCreateDate(DESC)
                .build();
        testCases.add(Arguments.of(params, giftCertificateFilterDto));

        params = new HashMap<>();
        params.put(NAME, "certificate");
        params.put(SHOW_DISABLED, "true");
        params.put(DESCRIPTION, "certificate");
        params.put(TAG_NAMES, "one,two");
        params.put(ORDERING_NAME, "asc");
        params.put(ORDERING_CREATE_DATE, "desc");
        giftCertificateFilterDto = GiftCertificateFilterDto.builder()
                .name("certificate")
                .showDisabled(true)
                .description("certificate")
                .tagNames(List.of("one", "two"))
                .orderingName(ASC)
                .orderingCreateDate(DESC)
                .build();
        testCases.add(Arguments.of(params, giftCertificateFilterDto));

        return testCases;
    }
}
