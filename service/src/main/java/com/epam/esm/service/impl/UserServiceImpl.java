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
        return userRepository.count();
    }

    @Override
    public List<UserDto> findAll(PageDto pageDto) {
        return userRepository.findAll(pageDto.toPageable())
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public TokenDto signUp(UserSignUpDto userSignUpDto) {
        String login = userSignUpDto.getLogin();
        if (userRepository.findByLogin(login).isPresent()) {
            throw new InvalidEntityException(List.of(NOT_UNIQUE_USER_LOGIN), UserSignUpDto.class);
        }

        List<ValidationError> validationErrors = userValidator.validateWithRequiredParams(userSignUpDto);
        if (!validationErrors.isEmpty()) {
            throw new InvalidEntityException(validationErrors, UserSignUpDto.class);
        }

        User user = buildUser(userSignUpDto, USER);
        userRepository.save(user);

        authenticateUser(user);

        return generateTokenDto(user);
    }

    @Override
    @Transactional
    public TokenDto login(UserSignInDto userSignInDto) {
        User user = userRepository.findByLogin(userSignInDto.getLogin())
                .orElseThrow(() -> new UserAuthenticationException(NO_USER_WITH_SUCH_LOGIN));

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
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, UserDto.class));
        return userMapper.toDto(user);
    }

    @Override
    public UserDto findByLogin(String login) {
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new EntityNotFoundException(UserDto.class));
        return userMapper.toDto(user);
    }

    @Override
    public String getUserPassword(UserDto userDto) {
        long id = userDto.getId();
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, UserDto.class));
        return user.getPassword();
    }

    @Override
    public Long countAllUserOrders(long userId) {
        return userRepository.countAllUserOrders(userId);
    }

    @Override
    public List<UserOrderResponseDto> findUserOrders(long userId, PageDto pageDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(userId, UserDto.class));
        return userRepository.findUserOrders(userId, pageDto.toPageable()).stream()
                .map(userOrderResponseDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserOrderResponseDto findUserOrder(long userId, long orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->new EntityNotFoundException(orderId, UserOrderResponseDto.class));
        if (order.getUser().getId() != userId) {
            throw new EntityNotFoundException(orderId, UserOrderResponseDto.class);
        }
        return userOrderResponseDtoMapper.toDto(order);
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
        user.setRole(userRoleRepository.findUserRoleByName(userRoleName));

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
