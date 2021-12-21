package com.epam.esm.service.impl;

import com.epam.esm.dto.*;
import com.epam.esm.dto.mapping.UserDtoMapper;
import com.epam.esm.dto.mapping.UserOrderResponseDtoMapper;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.RefreshToken;
import com.epam.esm.entity.User;
import com.epam.esm.entity.UserRoleName;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.InvalidEntityException;
import com.epam.esm.exception.UserAuthenticationException;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.repository.UserRoleRepository;
import com.epam.esm.service.RefreshTokenService;
import com.epam.esm.service.UserService;
import com.epam.esm.validator.UserValidator;
import com.epam.esm.validator.ValidationError;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.epam.esm.entity.UserRoleName.USER;
import static com.epam.esm.exception.UserAuthenticationException.State.INVALID_PASSWORD;
import static com.epam.esm.exception.UserAuthenticationException.State.NO_USER_WITH_SUCH_LOGIN;
import static com.epam.esm.validator.ValidationError.NOT_UNIQUE_USER_LOGIN;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final UserRoleRepository userRoleRepository;
    private final OrderRepository orderRepository;
    private final RefreshTokenService refreshTokenService;
    private final UserDtoMapper userMapper;
    private final UserOrderResponseDtoMapper userOrderResponseDtoMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtServiceImpl jwtGenerator;

    @Override
    public Long countAll() {
        return userRepository.countAll();
    }

    @Override
    public List<UserDto> findAll(Integer pageNumber, Integer pageSize) {
        return userRepository.findAll(pageNumber, pageSize)
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public TokenDto signUp(UserSignUpDto userSignUpDto) {
        String login = userSignUpDto.getLogin();
        if (userRepository.findByLogin(login).isPresent()) {
            throw new InvalidEntityException(List.of(NOT_UNIQUE_USER_LOGIN), UserSignUpDto.class);
        }

        List<ValidationError> validationErrors = userValidator.validateWithRequiredParams(userSignUpDto);
        if (validationErrors.size() != 0) {
            throw new InvalidEntityException(validationErrors, UserSignUpDto.class);
        }

        User user = buildUser(userSignUpDto, USER);
        userRepository.create(user);

        authenticateUser(user);

        return generateTokenDto(user);
    }

    @Override
    @Transactional
    public TokenDto login(UserSignInDto userSignInDto) {
        Optional<User> optionalUser = userRepository.findByLogin(userSignInDto.getLogin());
        if (optionalUser.isEmpty()) {
            throw new UserAuthenticationException(NO_USER_WITH_SUCH_LOGIN);
        }
        User user = optionalUser.get();

        String encodedPassword = DigestUtils.sha256Hex(userSignInDto.getPassword());
        if (!encodedPassword.equals(user.getPassword())) {
            throw new UserAuthenticationException(INVALID_PASSWORD);
        }

        authenticateUser(user);

        return generateTokenDto(user);
    }

    @Override
    @Transactional
    public UserDto findById(long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return userMapper.toDto(user.get());
        }
        else {
            throw new EntityNotFoundException(id, UserDto.class);
        }
    }

    @Override
    public UserDto findByLogin(String login) {
        Optional<User> user = userRepository.findByLogin(login);
        if (user.isPresent()) {
            return userMapper.toDto(user.get());
        }
        else {
            throw new EntityNotFoundException(UserDto.class);
        }
    }

    @Override
    public String getUserPassword(UserDto userDto) {
        Optional<User> user = userRepository.findById(userDto.getId());
        if (user.isPresent()) {
            return user.get().getPassword();
        }
        else {
            throw new EntityNotFoundException(userDto.getId(), UserDto.class);
        }
    }

    @Override
    public Long countAllUserOrders(long id) {
        return userRepository.countAllUserOrders(id);
    }

    @Override
    public List<UserOrderResponseDto> findUserOrders(long id, Integer pageNumber, Integer pageSize) {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) {
            throw new EntityNotFoundException(id, UserDto.class);
        }
        List<Order> orders = userRepository.findUserOrders(id, pageNumber, pageSize);
        return orders.stream()
                .map(userOrderResponseDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserOrderResponseDto findUserOrder(long userId, long orderId){
        Optional<Order> order = orderRepository.findById(orderId);
        if (!order.isPresent()) {
            throw new EntityNotFoundException(orderId, UserOrderResponseDto.class);
        }
        if (order.get().getUser().getId() != userId) {
            throw new EntityNotFoundException(orderId, UserOrderResponseDto.class);
        }
        return userOrderResponseDtoMapper.toDto(order.get());
    }

    private User buildUser(UserSignUpDto userSignUpDto, UserRoleName userRoleName) {
        User user = new User();

        String login = userSignUpDto.getLogin();
        String password = userSignUpDto.getPassword();
        String name = userSignUpDto.getName();
        String surname = userSignUpDto.getSurname();
        String email = userSignUpDto.getEmail();

        password = DigestUtils.sha256Hex(password);

        user.setLogin(login);
        user.setPassword(password);
        user.setName(name);
        user.setSurname(surname);
        user.setEmail(email);
        user.setRole(userRoleRepository.findUserRoleByName(USER));

        return user;
    }

    private void authenticateUser(User user) {
        UsernamePasswordAuthenticationToken authRequest;
        authRequest = new UsernamePasswordAuthenticationToken(user.getLogin(), user.getPassword());

        Authentication authentication = authenticationManager.authenticate(authRequest);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
    }

    private TokenDto generateTokenDto(User user) {
        String jwt = jwtGenerator.generateJwtToken(user.getLogin());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        return TokenDto.builder()
                .accessToken(jwt)
                .refreshToken(refreshToken.getToken())
                .build();
    }
}
