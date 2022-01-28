package com.epam.esm.controller;

import com.epam.esm.dto.OrderCreateRequestDto;
import com.epam.esm.dto.OrderItemDto;
import com.epam.esm.dto.OrderResponseDto;
import com.epam.esm.dto.OrderUpdateRequestDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Map;

@RequestMapping("/api/v1/orders")
public interface OrderController extends PagedController<OrderResponseDto> {

    @ApiOperation(value = "Get list of All GiftCertificate orders", response = PagedModel.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list of All GiftCertificates orders"),
            @ApiResponse(code = 400, message = "The GiftCertificate order can't be fetched due to bad request"),
            @ApiResponse(code = 404, message = "The GiftCertificate order you were trying to reach is not found")
    }
    )
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    ResponseEntity<PagedModel<OrderResponseDto>> getAll(@ApiParam(value = "The Tag params") @RequestParam Map<String, Object> params);

    @ApiOperation(value = "Get GiftCertificate order", response = OrderResponseDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the GiftCertificate order"),
            @ApiResponse(code = 400, message = "The GiftCertificate order can't be fetched due to bad request"),
            @ApiResponse(code = 404, message = "The GiftCertificate order you were trying to reach is not found")
    }
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<OrderResponseDto> getOrder(@ApiParam(value = "The Order ID") @PathVariable("id") @Min(1) long id);

    @ApiOperation(value = "Get OrderItem of Order", response = OrderItemDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the OrderItem"),
            @ApiResponse(code = 400, message = "The OrderItem can't be fetched due to bad request"),
            @ApiResponse(code = 404, message = "The OrderItem order you were trying to reach is not found")
    }
    )
    @GetMapping("/{orderId}/orderItems/{orderItemId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    ResponseEntity<OrderItemDto> getOrderItem(@ApiParam(value = "The Order ID") @PathVariable("orderId") @Min(1) long orderId,
                                              @ApiParam(value = "The Order Item ID") @PathVariable("orderItemId") @Min(1) long orderItemId);

    @ApiOperation(value = "Create GiftCertificate order", response = OrderResponseDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created a GiftCertificate order"),
            @ApiResponse(code = 400, message = "The GiftCertificate order can't be created due to bad request"),
            @ApiResponse(code = 409, message = "The GiftCertificate order can't be created due to a conflict with the current state of the resource.")
    }
    )
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    ResponseEntity<OrderResponseDto> createOrder(@ApiParam(value = "The Order create request dto") @RequestBody @NotNull OrderCreateRequestDto orderCreateRequestDto);

    @ApiOperation(value = "Update GiftCertificate order", response = OrderResponseDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated a GiftCertificate order"),
            @ApiResponse(code = 400, message = "The GiftCertificate order can't be updated due to bad request"),
            @ApiResponse(code = 409, message = "The GiftCertificate order can't be updated due to a conflict with the current state of the resource.")
    }
    )
    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    ResponseEntity<OrderResponseDto> updateOrder(@ApiParam(value = "The Order ID") @PathVariable("id") @Min(1) long id,
                                                 @ApiParam(value = "The Order update request dto") @RequestBody @NotNull OrderUpdateRequestDto orderUpdateRequestDto);

    @ApiOperation(value = "Delete GiftCertificate order")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successfully deleted a GiftCertificate order"),
            @ApiResponse(code = 400, message = "The GiftCertificate order can't be deleted due to bad request"),
            @ApiResponse(code = 404, message = "The GiftCertificate order is not found to be deleted")
    }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    ResponseEntity<Void> deleteOrder(@ApiParam(value = "The Order ID") @PathVariable("id") @Min(1) long id);
}
