package com.epam.esm.controller.impl;

import com.epam.esm.controller.OrderController;
import com.epam.esm.dto.OrderCreateRequestDto;
import com.epam.esm.dto.OrderItemDto;
import com.epam.esm.dto.OrderResponseDto;
import com.epam.esm.dto.OrderUpdateRequestDto;
import com.epam.esm.dto.PageDto;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.provider.PageModelProvider;
import com.epam.esm.provider.impl.OrderItemLinksProvider;
import com.epam.esm.provider.impl.OrderResponseLinksProvider;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderControllerImpl implements OrderController {
    private final OrderService orderService;
    private final RequestService requestService;
    private final OrderResponseLinksProvider orderResponseLinksProvider;
    private final OrderItemLinksProvider orderItemLinksProvider;
    private final PageModelProvider pageModelProvider;

    @Override
    public ResponseEntity<PagedModel<OrderResponseDto>> getAll(@RequestParam Map<String, Object> params) {

        long ordersAmount = orderService.countAll();
        PageDto pageDto = requestService.createPageDTO(params, ordersAmount);

        List<OrderResponseDto> ordersDto = orderService.findAll(pageDto);
        ordersDto.forEach(orderDto -> orderDto.add(orderResponseLinksProvider.provide(orderDto)));

        PagedModel<OrderResponseDto> pagedModel = pageModelProvider.provide(this.getClass(),
                params, ordersDto,
                ordersAmount, pageDto);

        return new ResponseEntity<>(pagedModel, OK);
    }

    @Override
    public ResponseEntity<OrderResponseDto> getOrder(@PathVariable("id") @Min(1) long id) {
        OrderResponseDto orderDto = orderService.findById(id);
        orderDto.add(orderResponseLinksProvider.provide(orderDto));
        return new ResponseEntity<>(orderDto, OK);
    }

    @Override
    public ResponseEntity<OrderItemDto> getOrderItem(@PathVariable("orderId") @Min(1) long orderId,
                                                     @PathVariable("orderItemId") @Min(1) long orderItemId) {
        OrderResponseDto orderDto = orderService.findById(orderId);
        List<OrderItemDto> orderItemsDto = orderDto.getOrderGiftCertificates().stream()
                .filter(orderItemDto -> orderItemDto.getId() == orderItemId)
                .collect(Collectors.toList());
        if (orderItemsDto.size() < 1) {
            throw new EntityNotFoundException(orderItemId, OrderItemDto.class);
        }

        OrderItemDto orderItemDto = orderItemsDto.get(0);
        orderItemDto.add(orderItemLinksProvider.provide(orderId, orderItemDto));
        return new ResponseEntity<>(orderItemDto, OK);
    }

    @Override
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody @NotNull OrderCreateRequestDto orderCreateRequestDto) {
        OrderResponseDto createdOrderDto = orderService.create(orderCreateRequestDto);
        createdOrderDto.add(orderResponseLinksProvider.provide(createdOrderDto));
        return new ResponseEntity<>(createdOrderDto, CREATED);
    }

    @Override
    public ResponseEntity<OrderResponseDto> updateOrder(@PathVariable("id") @Min(1) long id,
                                                        @RequestBody @NotNull OrderUpdateRequestDto orderUpdateRequestDto) {
        OrderResponseDto updatedOrderDto = orderService.update(id, orderUpdateRequestDto);
        updatedOrderDto.add(orderResponseLinksProvider.provide(updatedOrderDto));
        return new ResponseEntity<>(updatedOrderDto, OK);
    }

    @Override
    public ResponseEntity<Void> deleteOrder(@PathVariable("id") @Min(1) long id) {
        orderService.delete(id);
        return new ResponseEntity<>(NO_CONTENT);
    }
}
