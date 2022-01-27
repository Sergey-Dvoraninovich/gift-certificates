package com.epam.esm.service;

import com.epam.esm.dto.PageDto;
import com.epam.esm.dto.TokenDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.UserOrderResponseDto;
import com.epam.esm.dto.UserSignInDto;
import com.epam.esm.dto.UserSignUpDto;
import com.epam.esm.dto.mapping.UserDtoMapper;
import com.epam.esm.dto.mapping.UserOrderResponseDtoMapper;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.OrderItem;
import com.epam.esm.entity.RefreshToken;
import com.epam.esm.entity.User;
import com.epam.esm.entity.UserRole;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.InvalidEntityException;
import com.epam.esm.exception.UserAuthenticationException;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.repository.UserRoleRepository;
import com.epam.esm.service.impl.JwtServiceImpl;
import com.epam.esm.service.impl.UserServiceImpl;
import com.epam.esm.validator.UserValidator;
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
import org.springframework.security.authentication.AuthenticationManager;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.entity.UserRoleName.USER;
import static com.epam.esm.validator.ValidationError.INVALID_USER_LOGIN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private static final Integer PAGE_NUMBER = 1;
    private static final Integer PAGE_SIZE = 10;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserRoleRepository userRoleRepository;
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserValidator userValidator;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private JwtServiceImpl jwtGenerator;

    @Mock
    private UserDtoMapper userDtoMapper;
    @Mock
    private UserOrderResponseDtoMapper userOrderResponseDtoMapper;

    @Mock
    private AuthenticationManager authenticationManager;

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
    void testSignUp() {
        TokenDto expectedTokenDto = provideTokenDto();
        UserSignUpDto userSignUpDto = provideSignUpDto();
        User user = provideSignedUpUser();

        when(userRepository.findByLogin(userSignUpDto.getLogin())).thenReturn(Optional.empty());
        when(userValidator.validateWithRequiredParams(userSignUpDto)).thenReturn(Collections.emptyList());
        when(userRoleRepository.findUserRoleByName(USER)).thenReturn(provideRole());
        when(userRepository.save(user)).thenReturn(user);

        when(authenticationManager.authenticate(any())).thenReturn(null);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(expectedTokenDto.getRefreshToken());
        refreshToken.setUser(user);
        when(jwtGenerator.generateJwtToken(user.getLogin())).thenReturn(expectedTokenDto.getAccessToken());
        when(refreshTokenService.createRefreshToken(user.getId())).thenReturn(refreshToken);

        TokenDto actualTokenDto = userService.signUp(userSignUpDto);

        assertEquals(expectedTokenDto, actualTokenDto);
    }

    @Test
    void testSignUpUserExists() {
        UserSignUpDto userSignUpDto = provideSignUpDto();
        User user = provideSignedUpUser();

        when(userRepository.findByLogin(userSignUpDto.getLogin())).thenReturn(Optional.of(user));

        try {
            userService.signUp(userSignUpDto);
            fail();
        } catch (InvalidEntityException e) {
            assertTrue(true);
        }

    }

    @Test
    void testSignUpInvalidUser() {
        UserSignUpDto userSignUpDto = provideSignUpDto();
        userSignUpDto.setLogin(userSignUpDto.getLogin() + "///");

        when(userRepository.findByLogin(userSignUpDto.getLogin())).thenReturn(Optional.empty());
        when(userValidator.validateWithRequiredParams(userSignUpDto)).thenReturn(Collections.singletonList(INVALID_USER_LOGIN));

        try {
            userService.signUp(userSignUpDto);
            fail();
        } catch (InvalidEntityException e) {
            assertTrue(true);
        }

    }

    @Test
    void testLogIn() {
        TokenDto expectedTokenDto = provideTokenDto();
        UserSignInDto userSignInDto = provideSignInDto();
        User user = provideSignedInUser();
        user.setId(21L);

        when(userRepository.findByLogin(userSignInDto.getLogin())).thenReturn(Optional.of(user));

        when(authenticationManager.authenticate(any())).thenReturn(null);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(expectedTokenDto.getRefreshToken());
        refreshToken.setUser(user);
        when(jwtGenerator.generateJwtToken(user.getLogin())).thenReturn(expectedTokenDto.getAccessToken());
        when(refreshTokenService.createRefreshToken(user.getId())).thenReturn(refreshToken);

        TokenDto actualTokenDto = userService.login(userSignInDto);

        assertEquals(expectedTokenDto, actualTokenDto);
    }

    @Test
    void testLogInNoUser() {
        UserSignInDto userSignInDto = provideSignInDto();
        userSignInDto.setPassword(userSignInDto.getPassword() + "!");

        when(userRepository.findByLogin(userSignInDto.getLogin())).thenReturn(Optional.empty());

        try {
            userService.login(userSignInDto);
            fail();
        } catch (UserAuthenticationException e) {
            assertTrue(true);
        }
    }

    @Test
    void testLogInWrongPassword() {
        UserSignInDto userSignInDto = provideSignInDto();
        userSignInDto.setPassword(userSignInDto.getPassword() + "!");
        User user = provideSignedInUser();
        user.setId(21L);

        when(userRepository.findByLogin(userSignInDto.getLogin())).thenReturn(Optional.of(user));

        try {
            userService.login(userSignInDto);
            fail();
        } catch (UserAuthenticationException e) {
            assertTrue(true);
        }
    }

    @Test
    void testFindByIdNotFound() {
        long nonExistingId = 9999999L;
        when(userRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        try {
            userService.findById(nonExistingId);
            fail();
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
            fail();
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
            fail();
        } catch (EntityNotFoundException e) {
            assertTrue(true);
        }
    }

    private UserSignUpDto provideSignUpDto() {
        return UserSignUpDto.builder()
                .login("user_test")
                .password("Password1234")
                .name("Alex")
                .surname("Piterson")
                .email("alex.piterson@gmail.com")
                .build();
    }

    private UserSignInDto provideSignInDto() {
        return UserSignInDto.builder()
                .login("user_test")
                .password("Password1234")
                .build();
    }

    private TokenDto provideTokenDto() {
        return TokenDto.builder()
                .accessToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNjQzMTQ1NTc2LCJleHAiOjE2NDMxNTE1NzZ9.2JuBbOB60iVnstrKMLYetkJjMd7jui8R30q9P5JoqQQWMB_nVDNj2nJiYjtJgRfqiVy6jJj_172JBJyg1v2_1w")
                .refreshToken("faf3485a-e4e5-472f-b5c5-4a66ee44d938")
                .build();
    }

    private UserRole provideRole() {
        UserRole role = new UserRole();
        role.setId(1);
        role.setName(USER);
        return role;
    }

    private User provideSignedUpUser() {
        User user = new User();
        user.setLogin("user_test");
        user.setPassword("a0f3285b07c26c0dcd2191447f391170d06035e8d57e31a048ba87074f3a9a15");
        user.setName("Alex");
        user.setSurname("Piterson");
        user.setEmail("alex.piterson@gmail.com");
        user.setRole(provideRole());
        return user;
    }

    private User provideSignedInUser() {
        User user = new User();
        user.setLogin("user_test");
        user.setPassword("a0f3285b07c26c0dcd2191447f391170d06035e8d57e31a048ba87074f3a9a15");
        user.setName("Alex");
        user.setSurname("Piterson");
        user.setEmail("alex.piterson@gmail.com");
        user.setRole(provideRole());
        return user;
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
