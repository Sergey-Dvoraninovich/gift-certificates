package com.epam.esm.controller;

import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.UserOrderResponseDto;
import com.epam.esm.exception.InvalidPaginationException;
import com.epam.esm.hateos.TagHateoas;
import com.epam.esm.hateos.UserListHateoas;
import com.epam.esm.hateos.provider.impl.TagHateoasProvider;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.Collections;
import java.util.List;

import static com.epam.esm.validator.ValidationError.PAGE_IS_OUT_OF_RANGE;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TagHateoasProvider tagHateoasProvider;
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
    public ResponseEntity<UserListHateoas> getUsers(@ApiParam(value = "pageNumber", required = false) @RequestParam(value = "pageNumber", defaultValue = "1") @Min(1) Integer pageNumber,
                                                    @ApiParam(value = "pageNumber", required = false) @RequestParam(value = "pageNumber", defaultValue = "10") @Min(1) Integer pageSize) {
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

    @ApiOperation(value = "Get User", response = UserDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the User"),
            @ApiResponse(code = 400, message = "The User can't be fetched due to bad request"),
            @ApiResponse(code = 404, message = "The User you were trying to reach is not found")
    }
    )
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@ApiParam(value = "The User ID") @PathVariable("id") @Min(1) long id) {
        UserDto userDto = userService.findById(id);
        return new ResponseEntity<>(userDto, OK);
    }

    @ApiOperation(value = "Get list of Orders of user with specified ID", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list of user Orders"),
            @ApiResponse(code = 400, message = "The user Orders can't be fetched due to bad request"),
            @ApiResponse(code = 404, message = "The user Orders you were trying to reach is not found")
    }
    )
    @GetMapping("/{id}/orders")
    public ResponseEntity<List<UserOrderResponseDto>> getUserOrders(@ApiParam(value = "The User ID") @PathVariable("id") @Min(1) long id,
                                                                    @ApiParam(value = "pageNumber", required = false) @RequestParam(value = "pageNumber", defaultValue = "1") @Min(1) Integer pageNumber,
                                                                    @ApiParam(value = "pageNumber", required = false) @RequestParam(value = "pageNumber", defaultValue = "10") @Min(1) Integer pageSize) {
        List<UserOrderResponseDto> ordersDto = userService.findUserOrders(id);
        return new ResponseEntity<>(ordersDto, OK);
    }

    @ApiOperation(value = "Get most widely used User tag with highest price", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list of user Orders"),
            @ApiResponse(code = 400, message = "The user Orders can't be fetched due to bad request"),
            @ApiResponse(code = 404, message = "The user Orders you were trying to reach is not found")
    }
    )
    @GetMapping("/{id}/mostWidelyUsedTag")
    public ResponseEntity<TagHateoas> getUserOrders(@ApiParam(value = "The User ID") @PathVariable("id") @Min(1) long id) {
        TagDto tagDto = userService.findMostWidelyUsedTag(id);
        TagHateoas tagHateoas = TagHateoas.build(tagDto, tagHateoasProvider);
        return new ResponseEntity<>(tagHateoas, OK);
    }
}
