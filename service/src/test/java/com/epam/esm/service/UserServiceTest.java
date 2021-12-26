package com.epam.esm.service;

import com.epam.esm.dto.PageDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.UserOrderResponseDto;
import com.epam.esm.dto.mapping.UserDtoMapper;
import com.epam.esm.dto.mapping.UserOrderResponseDtoMapper;
import com.epam.esm.entity.*;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.impl.UserServiceImpl;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    private static final Integer PAGE_NUMBER = 1;
    private static final Integer PAGE_SIZE = 10;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserDtoMapper userDtoMapper;
    @Mock
    private UserOrderResponseDtoMapper userOrderResponseDtoMapper;

    @BeforeAll
    static void setUp() {
        MockitoAnnotations.openMocks(UserServiceTest.class);
    }

    @Test
    void testFindAll() {
        List<User> users = provideUsersList();
        List<UserDto> usersDto = provideUsersDtoList();
        Page<User> usersPage = new PageImpl<>(users);
        when(userRepository.findAll(PageRequest.of(PAGE_NUMBER - 1, PAGE_SIZE))).thenReturn(usersPage);
        for (int i = 0; i < users.size(); i++) {
            when(userDtoMapper.toDto(users.get(i))).thenReturn(usersDto.get(i));
        }

        List<UserDto> expectedDtoList = provideUsersDtoList();
        List<UserDto> actualDtoList = userService.findAll(new PageDto(PAGE_NUMBER, PAGE_SIZE));

        assertEquals(expectedDtoList, actualDtoList);
    }

    @Test
    void testFindById() {
        User user = provideUsersList().get(0);
        UserDto userDto = provideUsersDtoList().get(0);
        when(userRepository.findById(userDto.getId())).thenReturn(Optional.of(user));
        when(userDtoMapper.toDto(user)).thenReturn(userDto);

        UserDto actualUserDto = userService.findById(userDto.getId());

        assertEquals(userDto, actualUserDto);
    }

    @Test
    void testFindByIdNotFound() {
        long nonExistingId = 9999999L;
        when(userRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        try {
            userService.findById(nonExistingId);
            assertTrue(false);
        } catch (EntityNotFoundException e) {
            assertTrue(true);
        }
    }

    @Test
    void testFindUserOrdersNoUser() {
        long nonExistingId = 9999999L;
        when(userRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        try {
            userService.findById(nonExistingId);
            assertTrue(false);
        } catch (EntityNotFoundException e) {
            assertTrue(true);
        }
    }

    @Test
    void testFindUserOrders() {
        User user = provideUsersList().get(0);
        List<Order> userOrders = provideUserOrdersList();
        List<UserOrderResponseDto> userOrdersDto = provideUserOrderResponsesDtoList();
        Page<Order> userOrdersPage = new PageImpl<>(userOrders);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.findUserOrders(user.getId(),PageRequest.of(PAGE_NUMBER - 1, PAGE_SIZE))).thenReturn(userOrdersPage);
        for (int i = 0; i < userOrders.size(); i++) {
            when(userOrderResponseDtoMapper.toDto(userOrders.get(i))).thenReturn(userOrdersDto.get(i));
        }

        List<UserOrderResponseDto> actualOrdersDto = userService.findUserOrders(user.getId(), new PageDto(PAGE_NUMBER, PAGE_SIZE));

        assertEquals(userOrdersDto, actualOrdersDto);
    }

    @Test
    void testFindUserOrder() {
        User user = provideUsersList().get(0);
        Order userOrder = provideUserOrdersList().get(0);
        UserOrderResponseDto userOrderDto = provideUserOrderResponsesDtoList().get(0);

        when(orderRepository.findById(userOrder.getId())).thenReturn(Optional.of(userOrder));
        when(userOrderResponseDtoMapper.toDto(userOrder)).thenReturn(userOrderDto);

        UserOrderResponseDto actualOrderDto = userService.findUserOrder(user.getId(), userOrder.getId());

        assertEquals(userOrderDto, actualOrderDto);
    }

    @Test
    void testFindUserOrderWrongUser() {
        User wrongUser = provideUsersList().get(1);
        Order userOrder = provideUserOrdersList().get(0);

        when(orderRepository.findById(userOrder.getId())).thenReturn(Optional.of(userOrder));

        try {
            userService.findUserOrder(wrongUser.getId(), userOrder.getId());
            assertTrue(false);
        } catch (EntityNotFoundException e) {
            assertTrue(true);
        }
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

    private List<UserOrderResponseDto> provideUserOrderResponsesDtoList() {
        Instant date = Instant.from(ZonedDateTime.of(2000, 1, 1, 11, 11, 11, 222000000, ZoneId.of("Europe/Minsk")));

        UserOrderResponseDto firstOrder = UserOrderResponseDto.builder()
                .id(1L)
                .count(1)
                .totalPrice(new BigDecimal("100.00"))
                .createOrderTime(date)
                .updateOrderTime(date)
                .build();

        UserOrderResponseDto secondOrder = UserOrderResponseDto.builder()
                .id(2L)
                .count(1)
                .totalPrice(new BigDecimal("100.00"))
                .createOrderTime(date)
                .updateOrderTime(date)
                .build();

        return Arrays.asList(firstOrder, secondOrder);
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

    private List<Order> provideUserOrdersList() {
        Instant date = Instant.from(ZonedDateTime.of(2000, 1, 1, 11, 11, 11, 222000000, ZoneId.of("Europe/Minsk")));

        Order firstOrder = new Order();
        firstOrder.setId(1L);
        firstOrder.setUser(provideUsersList().get(0));
        firstOrder.setOrderItems(provideOrderItems());
        firstOrder.setCreateOrderTime(date);
        firstOrder.setUpdateOrderTime(date);

        Order secondOrder = new Order();
        secondOrder.setId(2L);
        secondOrder.setUser(provideUsersList().get(0));
        secondOrder.setOrderItems(provideOrderItems());
        secondOrder.setCreateOrderTime(date);
        secondOrder.setUpdateOrderTime(date);

        return Arrays.asList(firstOrder, secondOrder);
    }

    private List<OrderItem> provideOrderItems() {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(1L);
        giftCertificate.setName("certificate first and second tags");
        giftCertificate.setDescription("certificate with first tag and second tag");
        giftCertificate.setPrice(new BigDecimal("50.00"));
        giftCertificate.setDuration(Duration.ofDays(90));
        Instant date = Instant.from(ZonedDateTime.of(2000, 1, 1, 11, 11, 11, 222000000, ZoneId.of("Europe/Minsk")));
        giftCertificate.setCreateDate(date);
        giftCertificate.setLastUpdateDate(date);

        OrderItem orderItem = new OrderItem();
        orderItem.setId(1L);
        orderItem.setPrice(new BigDecimal("100.00"));
        orderItem.setGiftCertificate(giftCertificate);

        return Collections.singletonList(orderItem);
    }
}
