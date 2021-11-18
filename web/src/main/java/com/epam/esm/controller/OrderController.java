package com.epam.esm.controller;

import com.epam.esm.dto.OrderCreateRequestDto;
import com.epam.esm.dto.OrderResponseDto;
import com.epam.esm.dto.OrderUpdateRequestDto;
import com.epam.esm.service.OrderService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @ApiOperation(value = "Get list of All GiftCertificate orders", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list of All GiftCertificates orders"),
            @ApiResponse(code = 400, message = "The GiftCertificate order can't be fetched due to bad request"),
            @ApiResponse(code = 404, message = "The GiftCertificate order you were trying to reach is not found")
    }
    )
    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getOrders(@ApiParam(value = "pageNumber", required = false) @RequestParam(value = "pageNumber", defaultValue = "1") @Min(1) Integer pageNumber,
                                                    @ApiParam(value = "pageNumber", required = false) @RequestParam(value = "pageNumber", defaultValue = "10") @Min(1) Integer pageSize,
                                                    @ApiParam(value = "sortOrder", required = false) @RequestParam(value = "sortOrder", defaultValue = "ASC") String sortOrder) {
        List<OrderResponseDto> ordersDto = orderService.findAll();
        return new ResponseEntity<>(ordersDto, OK);
    }

    @ApiOperation(value = "Get GiftCertificate order", response = OrderResponseDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the GiftCertificate order"),
            @ApiResponse(code = 400, message = "The GiftCertificate order can't be fetched due to bad request"),
            @ApiResponse(code = 404, message = "The GiftCertificate order you were trying to reach is not found")
    }
    )
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> getOrder(@ApiParam(value = "The Order ID") @PathVariable("id") @Min(1) long id) {
        OrderResponseDto orderDto = orderService.findById(id);
        return new ResponseEntity<>(orderDto, OK);
    }

    @ApiOperation(value = "Create GiftCertificate order", response = OrderResponseDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created a GiftCertificate order"),
            @ApiResponse(code = 400, message = "The GiftCertificate order can't be created due to bad request"),
            @ApiResponse(code = 409, message = "The GiftCertificate order can't be created due to a conflict with the current state of the resource.")
    }
    )
    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@ApiParam(value = "The Order create request dto") @RequestBody @NotNull OrderCreateRequestDto orderCreateRequestDto) {
        OrderResponseDto insertedOrderDto = orderService.create(orderCreateRequestDto);
        return new ResponseEntity<>(insertedOrderDto, CREATED);
    }

    @ApiOperation(value = "Update GiftCertificate order", response = OrderResponseDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated a GiftCertificate order"),
            @ApiResponse(code = 400, message = "The GiftCertificate order can't be updated due to bad request"),
            @ApiResponse(code = 409, message = "The GiftCertificate order can't be updated due to a conflict with the current state of the resource.")
    }
    )
    @PatchMapping("/{id}")
    public ResponseEntity<OrderResponseDto> updateOrder(@ApiParam(value = "The Order ID") @PathVariable("id") @Min(1) long id,
                                                        @ApiParam(value = "The Order update request dto") @RequestBody @NotNull OrderUpdateRequestDto orderUpdateRequestDto) {
        OrderResponseDto updatedOrderDto = orderService.update(id, orderUpdateRequestDto);
        return new ResponseEntity<>(updatedOrderDto, OK);
    }

    @ApiOperation(value = "Delete GiftCertificate order")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successfully deleted a GiftCertificate order"),
            @ApiResponse(code = 400, message = "The GiftCertificate order can't be deleted due to bad request"),
            @ApiResponse(code = 404, message = "The GiftCertificate order is not found to be deleted")
    }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@ApiParam(value = "The Order ID") @PathVariable("id") @Min(1) long id) {
        orderService.delete(id);
        return new ResponseEntity<>(NO_CONTENT);
    }
}
