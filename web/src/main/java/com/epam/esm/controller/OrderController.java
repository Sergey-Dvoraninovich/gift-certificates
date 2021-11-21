package com.epam.esm.controller;

import com.epam.esm.dto.OrderCreateRequestDto;
import com.epam.esm.dto.OrderResponseDto;
import com.epam.esm.dto.OrderUpdateRequestDto;
import com.epam.esm.exception.InvalidPaginationException;
import com.epam.esm.hateos.OrderResponseHateoas;
import com.epam.esm.hateos.OrderResponseListHateoas;
import com.epam.esm.hateos.provider.impl.OrderResponseHateoasProvider;
import com.epam.esm.service.OrderService;
import com.epam.esm.validator.PaginationValidator;
import com.epam.esm.validator.ValidationError;
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
import java.util.Collections;
import java.util.List;

import static com.epam.esm.validator.ValidationError.PAGE_IS_OUT_OF_RANGE;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final PaginationValidator paginationValidator;
    private final OrderResponseHateoasProvider orderResponseHateoasProvider;

    @ApiOperation(value = "Get list of All GiftCertificate orders", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list of All GiftCertificates orders"),
            @ApiResponse(code = 400, message = "The GiftCertificate order can't be fetched due to bad request"),
            @ApiResponse(code = 404, message = "The GiftCertificate order you were trying to reach is not found")
    }
    )
    @GetMapping
    public ResponseEntity<OrderResponseListHateoas> getOrders(@ApiParam(value = "pageNumber", required = false) @RequestParam(value = "pageNumber", defaultValue = "1") @Min(1) Integer pageNumber,
                                                              @ApiParam(value = "pageSize", required = false) @RequestParam(value = "pageSize", defaultValue = "10") @Min(1) Integer pageSize,
                                                              @ApiParam(value = "sortOrder", required = false) @RequestParam(value = "sortOrder", defaultValue = "ASC") String sortOrder) {
        List<ValidationError> validationErrors = paginationValidator.validateParams(pageNumber, pageSize);
        if (!validationErrors.isEmpty()) {
            throw new InvalidPaginationException(pageNumber, pageSize, validationErrors);
        }

        Long ordersDtoAmount = orderService.countAll();
        if (ordersDtoAmount <= (pageNumber - 1) * pageSize) {
            throw new InvalidPaginationException(pageNumber, pageSize, Collections.singletonList(PAGE_IS_OUT_OF_RANGE));
        }

        List<OrderResponseDto> ordersDto = orderService.findAll(sortOrder, pageNumber, pageSize);
        OrderResponseListHateoas ordersHateoas = OrderResponseListHateoas.build(ordersDto, orderResponseHateoasProvider,
                ordersDtoAmount, pageNumber, pageSize, sortOrder);
        return new ResponseEntity<>(ordersHateoas, OK);
    }

    @ApiOperation(value = "Get GiftCertificate order", response = OrderResponseDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the GiftCertificate order"),
            @ApiResponse(code = 400, message = "The GiftCertificate order can't be fetched due to bad request"),
            @ApiResponse(code = 404, message = "The GiftCertificate order you were trying to reach is not found")
    }
    )
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseHateoas> getOrder(@ApiParam(value = "The Order ID") @PathVariable("id") @Min(1) long id) {
        OrderResponseDto orderDto = orderService.findById(id);
        OrderResponseHateoas orderResponseHateoas = OrderResponseHateoas.build(orderDto, orderResponseHateoasProvider);
        return new ResponseEntity<>(orderResponseHateoas, OK);
    }

    @ApiOperation(value = "Create GiftCertificate order", response = OrderResponseDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created a GiftCertificate order"),
            @ApiResponse(code = 400, message = "The GiftCertificate order can't be created due to bad request"),
            @ApiResponse(code = 409, message = "The GiftCertificate order can't be created due to a conflict with the current state of the resource.")
    }
    )
    @PostMapping
    public ResponseEntity<OrderResponseHateoas> createOrder(@ApiParam(value = "The Order create request dto") @RequestBody @NotNull OrderCreateRequestDto orderCreateRequestDto) {
        OrderResponseDto createdOrderDto = orderService.create(orderCreateRequestDto);
        OrderResponseHateoas orderResponseHateoas = OrderResponseHateoas.build(createdOrderDto, orderResponseHateoasProvider);
        return new ResponseEntity<>(orderResponseHateoas, CREATED);
    }

    @ApiOperation(value = "Update GiftCertificate order", response = OrderResponseDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated a GiftCertificate order"),
            @ApiResponse(code = 400, message = "The GiftCertificate order can't be updated due to bad request"),
            @ApiResponse(code = 409, message = "The GiftCertificate order can't be updated due to a conflict with the current state of the resource.")
    }
    )
    @PatchMapping("/{id}")
    public ResponseEntity<OrderResponseHateoas> updateOrder(@ApiParam(value = "The Order ID") @PathVariable("id") @Min(1) long id,
                                                        @ApiParam(value = "The Order update request dto") @RequestBody @NotNull OrderUpdateRequestDto orderUpdateRequestDto) {
        OrderResponseDto updatedOrderDto = orderService.update(id, orderUpdateRequestDto);
        OrderResponseHateoas orderResponseHateoas = OrderResponseHateoas.build(updatedOrderDto, orderResponseHateoasProvider);
        return new ResponseEntity<>(orderResponseHateoas, OK);
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
