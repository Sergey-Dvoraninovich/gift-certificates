package com.epam.esm.service;

import com.epam.esm.dto.*;
import com.epam.esm.dto.mapping.OrderResponseDtoMapper;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.OrderItem;
import com.epam.esm.entity.User;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.impl.OrderServiceImpl;
import com.epam.esm.validator.OrderCreateValidator;
import com.epam.esm.validator.OrderSearchParamsValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    private static final Integer PAGE_NUMBER = 1;
    private static final Integer PAGE_SIZE = 10;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private GiftCertificateRepository giftCertificateRepository;

    @Mock
    private OrderSearchParamsValidator orderSearchParamsValidator;
    @Mock
    private OrderCreateValidator orderCreateValidator;

    @Mock
    private OrderResponseDtoMapper orderResponseDtoMapper;

    @BeforeAll
    static void setUp() {
        MockitoAnnotations.openMocks(TagServiceTest.class);
    }

    @Test
    void testFindAll() {
        List<Order> orders = provideOrdersList();
        List<OrderResponseDto> ordersDto = provideOrdersDtoList();
        OrderFilterDto orderFilterDto = new OrderFilterDto();
        Page<Order> ordersPage = new PageImpl<>(orders);
        when(orderRepository.findAll(PageRequest.of(PAGE_NUMBER - 1, PAGE_SIZE))).thenReturn(ordersPage);
        for (int i = 0; i < orders.size(); i++) {
            when(orderResponseDtoMapper.toDto(orders.get(i))).thenReturn(ordersDto.get(i));
        }

        List<OrderResponseDto> actualDtoList = orderService.findAll(orderFilterDto, new PageDto(PAGE_NUMBER, PAGE_SIZE));

        assertEquals(ordersDto, actualDtoList);
    }

    @Test
    void testFindById() {
        Order order = provideOrdersList().get(0);
        OrderResponseDto orderDto = provideOrdersDtoList().get(0);
        when(orderRepository.findById(orderDto.getId())).thenReturn(Optional.of(order));
        when(orderResponseDtoMapper.toDto(order)).thenReturn(orderDto);

        OrderResponseDto actualOrderDto = orderService.findById(orderDto.getId());

        assertEquals(orderDto, actualOrderDto);
    }

    @Test
    void testFindByIdNotFound() {
        long nonExistingId = 9999999L;
        when(orderRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        try {
            orderService.findById(nonExistingId);
            assertTrue(false);
        } catch (EntityNotFoundException e) {
            assertTrue(true);
        }
    }

    @Test
    void testCreate() {
        OrderCreateRequestDto requestDto = provideOrderCreateRequestDto();
        OrderResponseDto responseDto = provideOrdersDtoList().get(0);
        Order order = provideOrdersList().get(0);
        List<OrderItem> orderItems = provideOrderItems();
        List<GiftCertificate> giftCertificates = provideGistCertificates();

        when(orderCreateValidator.validateWithRequiredParams(any(OrderCreateRequestDto.class))).thenReturn(Collections.emptyList());
        for (int i = 0; i < orderItems.size(); i++){
            when(giftCertificateRepository.findById(orderItems.get(i).getId())).thenReturn(Optional.of(giftCertificates.get(i)));
        }
        when(userRepository.findById(requestDto.getUserId())).thenReturn(Optional.of(provideUsersList().get(0)));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderResponseDtoMapper.toDto(order)).thenReturn(responseDto);

        OrderResponseDto actual = orderService.create(requestDto);

        assertEquals(responseDto, actual);
    }

    @Test
    void testDelete() {
        Order order = provideOrdersList().get(0);
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        orderService.delete(order.getId());
    }

    private List<OrderResponseDto> provideOrdersDtoList() {
        Instant date = Instant.from(ZonedDateTime.of(2021, 1, 1, 11, 11, 11, 222000000, ZoneId.of("Europe/Minsk")));

        OrderResponseDto firstOrder = OrderResponseDto.builder()
                .id(1L)
                .count(2)
                .user(provideUsersDtoList().get(0))
                .orderGiftCertificates(provideOrderItemsDto())
                .createOrderTime(date)
                .updateOrderTime(date)
                .build();

        OrderResponseDto secondOrder = OrderResponseDto.builder()
                .id(2L)
                .count(1)
                .user(provideUsersDtoList().get(1))
                .orderGiftCertificates(Collections.singletonList(provideOrderItemsDto().get(0)))
                .createOrderTime(date)
                .updateOrderTime(date)
                .build();

        return Arrays.asList(firstOrder, secondOrder);
    }

    private OrderCreateRequestDto provideOrderCreateRequestDto() {
        OrderCreateRequestDto order = OrderCreateRequestDto.builder()
                .userId(provideUsersDtoList().get(0).getId())
                .orderGiftCertificates(provideOrderItemsDto())
                .build();
        return order;
    }

    private List<Order> provideOrdersList() {
        Instant date = Instant.from(ZonedDateTime.of(2021, 1, 1, 11, 11, 11, 222000000, ZoneId.of("Europe/Minsk")));

        Order firstOrder = new Order();
        firstOrder.setId(1L);
        firstOrder.setUser(provideUsersList().get(0));
        firstOrder.setOrderItems(provideOrderItems());
        firstOrder.setCreateOrderTime(date);
        firstOrder.setUpdateOrderTime(date);

        Order secondOrder = new Order();
        secondOrder.setId(2L);
        secondOrder.setUser(provideUsersList().get(1));
        secondOrder.setOrderItems(Collections.singletonList(provideOrderItems().get(0)));
        secondOrder.setCreateOrderTime(date);
        secondOrder.setUpdateOrderTime(date);

        return Arrays.asList(firstOrder, secondOrder);
    }

    private List<UserDto> provideUsersDtoList() {
        UserDto firstUser = UserDto.builder()
                .id(1L)
                .login("ivan_ivanov")
                .name("Ivan")
                .surname("Ivanov")
                .email("ivan.ivanov@gmail.com")
                .build();

        UserDto secondUser = UserDto.builder()
                .id(2L)
                .login("petr_petrov")
                .name("Petr")
                .surname("Petrov")
                .email("petr.petrov@gmail.com")
                .build();

        return Arrays.asList(firstUser, secondUser);
    }

    private List<User> provideUsersList() {
        User firstUser = new User();
        firstUser.setId(1L);
        firstUser.setLogin("ivan_ivanov");
        firstUser.setName("Ivan");
        firstUser.setSurname("Ivanov");
        firstUser.setEmail("ivan.ivanov@gmail.com");

        User secondUser = new User();
        secondUser.setId(2L);
        secondUser.setLogin("petr_petrov");
        secondUser.setName("Petr");
        secondUser.setSurname("Petrov");
        secondUser.setEmail("petr.petrov@gmail.com");

        return Arrays.asList(firstUser, secondUser);
    }

    private List<OrderItemDto> provideOrderItemsDto() {

        OrderItemDto firstOrderItem = OrderItemDto.builder()
                .id(1L)
                .price(new BigDecimal("50.00"))
                .build();

        OrderItemDto secondOrderItem = OrderItemDto.builder()
                .id(2L)
                .price(new BigDecimal("100.00"))
                .build();

        return Arrays.asList(firstOrderItem, secondOrderItem);
    }

    private List<OrderItem> provideOrderItems() {

        OrderItem firstOrderItem = new OrderItem();
        firstOrderItem.setId(1L);
        firstOrderItem.setPrice(new BigDecimal("50.00"));
        firstOrderItem.setGiftCertificate(provideGistCertificates().get(0));

        OrderItem secondOrderItem = new OrderItem();
        secondOrderItem.setId(2L);
        secondOrderItem.setPrice(new BigDecimal("100.00"));
        secondOrderItem.setGiftCertificate(provideGistCertificates().get(1));

        return Arrays.asList(firstOrderItem, secondOrderItem);
    }

    private List<GiftCertificate> provideGistCertificates() {
        GiftCertificate firstCertificate = new GiftCertificate();
        firstCertificate.setId(1L);
        firstCertificate.setIsAvailable(true);
        firstCertificate.setName("certificate first and second tags");
        firstCertificate.setDescription("certificate with first tag and second tag");
        firstCertificate.setPrice(new BigDecimal("50.00"));
        firstCertificate.setDuration(Duration.ofDays(90));
        Instant firstDate = Instant.from(ZonedDateTime.of(2000, 1, 1, 11, 11, 11, 222000000, ZoneId.of("Europe/Minsk")));
        firstCertificate.setCreateDate(firstDate);
        firstCertificate.setLastUpdateDate(firstDate);

        GiftCertificate secondCertificate = new GiftCertificate();
        secondCertificate.setId(2L);
        secondCertificate.setIsAvailable(true);
        secondCertificate.setName("certificate second tags");
        secondCertificate.setDescription("certificate with second tag");
        secondCertificate.setPrice(new BigDecimal("100.00"));
        secondCertificate.setDuration(Duration.ofDays(180));
        Instant secondDate = Instant.from(ZonedDateTime.of(2011, 1, 1, 11, 11, 11, 222000000, ZoneId.of("Europe/Minsk")));
        secondCertificate.setCreateDate(secondDate);
        secondCertificate.setLastUpdateDate(secondDate);

        return Arrays.asList(firstCertificate, secondCertificate);
    }
}