package com.epam.esm.controller;

import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.UserOrderResponseDto;
import com.epam.esm.exception.InvalidPaginationException;
import com.epam.esm.hateos.UserHateoas;
import com.epam.esm.hateos.UserListHateoas;
import com.epam.esm.hateos.UserOrderResponseHateoas;
import com.epam.esm.hateos.UserOrderResponseListHateoas;
import com.epam.esm.hateos.provider.impl.UserHateoasProvider;
import com.epam.esm.service.UserService;
import com.epam.esm.validator.PaginationValidator;
import com.epam.esm.validator.ValidationError;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.epam.esm.validator.ValidationError.PAGE_IS_OUT_OF_RANGE;
import static org.springframework.http.HttpStatus.OK;


@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final String ACCESS_DENIED_FOR_USER = "Access denied for user";

    private final UserService userService;
    private final UserHateoasProvider userHateoasProvider;
    private final PaginationValidator paginationValidator;

    @ApiOperation(value = "Get list of Users", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list of Users"),
            @ApiResponse(code = 400, message = "The resource can't be fetched due to bad request"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<UserListHateoas> getUsers(@ApiParam(value = "pageNumber", required = false) @RequestParam(value = "pageNumber", defaultValue = "1") @Min(1) Integer pageNumber,
                                                    @ApiParam(value = "pageSize", required = false) @RequestParam(value = "pageSize", defaultValue = "10") @Min(1) Integer pageSize) {
        List<ValidationError> validationErrors = paginationValidator.validateParams(pageNumber, pageSize);
        if (!validationErrors.isEmpty()) {
            throw new InvalidPaginationException(pageNumber, pageSize, validationErrors);
        }

        Long usersDtoAmount = userService.countAll();
        if (usersDtoAmount <= (pageNumber - 1) * pageSize) {
            throw new InvalidPaginationException(pageNumber, pageSize, Collections.singletonList(PAGE_IS_OUT_OF_RANGE));
        }

        List<UserDto> usersDto = userService.findAll(pageNumber, pageSize);
        UserListHateoas userListHateoas = UserListHateoas.build(usersDto, userHateoasProvider,
                usersDtoAmount, pageNumber, pageSize);
        return new ResponseEntity<>(userListHateoas, OK);
    }

    @ApiOperation(value = "Get User", response = UserHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the User"),
            @ApiResponse(code = 400, message = "The User can't be fetched due to bad request"),
            @ApiResponse(code = 404, message = "The User you were trying to reach is not found")
    }
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<UserHateoas> getUser(@ApiParam(value = "The User ID") @PathVariable("id") @Min(1) long id) {
        UserDto userDto = userService.findById(id);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (containsAuthority(auth.getAuthorities(), "ROLE_USER")) {
            checkUserAuthority(userDto, auth.getName());
        }

        UserHateoas userHateoas = UserHateoas.build(userDto, userHateoasProvider);
        return new ResponseEntity<>(userHateoas, OK);
    }

    @ApiOperation(value = "Get list of Orders of user with specified ID", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list of user Orders"),
            @ApiResponse(code = 400, message = "The user Orders can't be fetched due to bad request"),
            @ApiResponse(code = 404, message = "The user Orders you were trying to reach is not found")
    }
    )
    @GetMapping("/{id}/orders")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<UserOrderResponseListHateoas> getUserOrders(@ApiParam(value = "The User ID") @PathVariable("id") @Min(1) long id,
                                                                      @ApiParam(value = "pageNumber", required = false) @RequestParam(value = "pageNumber", defaultValue = "1") @Min(1) Integer pageNumber,
                                                                      @ApiParam(value = "pageSize", required = false) @RequestParam(value = "pageSize", defaultValue = "10") @Min(1) Integer pageSize) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (containsAuthority(auth.getAuthorities(), "ROLE_USER")) {
            checkUserAuthority(id, auth.getName());
        }

        List<ValidationError> validationErrors = paginationValidator.validateParams(pageNumber, pageSize);
        if (!validationErrors.isEmpty()) {
            throw new InvalidPaginationException(pageNumber, pageSize, validationErrors);
        }

        Long ordersDtoAmount = userService.countAllUserOrders(id);
        if (ordersDtoAmount <= (pageNumber - 1) * pageSize) {
            throw new InvalidPaginationException(pageNumber, pageSize, Collections.singletonList(PAGE_IS_OUT_OF_RANGE));
        }

        List<UserOrderResponseDto> ordersDto = userService.findUserOrders(id, pageNumber, pageSize);
        UserOrderResponseListHateoas listHateoas = UserOrderResponseListHateoas.build(id, ordersDto, ordersDtoAmount, pageNumber, pageSize);
        return new ResponseEntity<>(listHateoas, OK);
    }

    @ApiOperation(value = "Get list of Orders of user with specified ID", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list of user Orders"),
            @ApiResponse(code = 400, message = "The user Orders can't be fetched due to bad request"),
            @ApiResponse(code = 404, message = "The user Orders you were trying to reach is not found")
    }
    )
    @GetMapping("/{userId}/orders/{orderId}")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<UserOrderResponseHateoas> getUserOrder(@ApiParam(value = "The User ID") @PathVariable("userId") @Min(1) long userId,
                                                              @ApiParam(value = "The Order ID") @PathVariable("orderId") @Min(1) long orderId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (containsAuthority(auth.getAuthorities(), "ROLE_USER")) {
            checkUserAuthority(userId, auth.getName());
        }

        UserOrderResponseDto order = userService.findUserOrder(userId, orderId);
        UserOrderResponseHateoas userOrderResponseHateoas = UserOrderResponseHateoas.build(userId, order);
        return new ResponseEntity<>(userOrderResponseHateoas, OK);
    }

    private void checkUserAuthority(long requestedUserId, String login) {
        UserDto userDto = userService.findById(requestedUserId);
        checkUserAuthority(userDto, login);
    }

    private void checkUserAuthority(UserDto requestedUserDto, String login){
        if (!requestedUserDto.getLogin().equals(login)) {
            throw new AccessDeniedException(ACCESS_DENIED_FOR_USER);
        }
    }

    private boolean containsAuthority(Collection<? extends GrantedAuthority> authorities, String providedAuthority) {
        return authorities.stream()
                .map(Object::toString)
                .filter(authorityLine -> authorityLine.equals(providedAuthority))
                .count() > 0;
    }
}
