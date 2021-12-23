package com.epam.esm.controller;

import com.epam.esm.dto.UserOrderResponseDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.Min;
import java.util.Map;

@RequestMapping("/api/v1/users/{userId}/orders")
public interface UserOrderController extends PagedItemsController<UserOrderResponseDto>{

    @ApiOperation(value = "Get list of Orders of user with specified ID", response = PagedModel.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list of user Orders"),
            @ApiResponse(code = 400, message = "The user Orders can't be fetched due to bad request"),
            @ApiResponse(code = 404, message = "The user Orders you were trying to reach is not found")
    }
    )
    @GetMapping()
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    ResponseEntity<PagedModel<UserOrderResponseDto>> getAllItems(@ApiParam(value = "The User ID") @PathVariable("userId") @Min(1) long userId,
                                                                 @ApiParam(value = "The User params", required = false) @RequestParam Map<String, Object> params);

    @ApiOperation(value = "Get list of Orders of user with specified ID", response = UserOrderResponseDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list of user Orders"),
            @ApiResponse(code = 400, message = "The user Orders can't be fetched due to bad request"),
            @ApiResponse(code = 404, message = "The user Orders you were trying to reach is not found")
    }
    )
    @GetMapping("/{userId}/orders/{orderId}")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    ResponseEntity<UserOrderResponseDto> getUserOrder(@ApiParam(value = "The User ID") @PathVariable("userId") @Min(1) long userId,
                                                      @ApiParam(value = "The Order ID") @PathVariable("orderId") @Min(1) long orderId);
}
