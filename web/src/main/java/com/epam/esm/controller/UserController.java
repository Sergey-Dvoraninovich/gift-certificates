package com.epam.esm.controller;

import com.epam.esm.dto.OrderResponseDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.UserOrderResponseDto;
import com.epam.esm.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @ApiOperation(value = "Get list of Users", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list of Users"),
            @ApiResponse(code = 400, message = "The resource can't be fetched due to bad request"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers(@ApiParam(value = "pageNumber", required = false) @RequestParam(value = "pageNumber", defaultValue = "1") @Min(1) Integer pageNumber,
                                                  @ApiParam(value = "pageNumber", required = false) @RequestParam(value = "pageNumber", defaultValue = "10") @Min(1) Integer pageSize) {
        List<UserDto> tagsDto = userService.findAll();
        return new ResponseEntity<>(tagsDto, OK);
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
}
