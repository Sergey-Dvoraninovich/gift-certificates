package com.epam.esm.controller.impl;

import com.epam.esm.controller.OrderController;
import com.epam.esm.dto.*;
import com.epam.esm.exception.AccessException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.provider.PageModelProvider;
import com.epam.esm.provider.impl.OrderItemLinksProvider;
import com.epam.esm.provider.impl.OrderResponseLinksProvider;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.RequestService;
import com.epam.esm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.epam.esm.exception.AccessException.State.INVALID_ORDER_USER;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderControllerImpl implements OrderController {

    private final OrderService orderService;
    private final RequestService requestService;
    private final UserService userService;
    private final OrderResponseLinksProvider orderResponseLinksProvider;
    private final OrderItemLinksProvider orderItemLinksProvider;
    private final PageModelProvider pageModelProvider;

    @Override
    public ResponseEntity<PagedModel<OrderResponseDto>> getAll(@RequestParam Map<String, Object> params) {

        long ordersAmount = orderService.countAll();
        PageDto pageDto = requestService.createPageDTO(params, ordersAmount);
        OrderFilterDto orderFilterDto = requestService.createOrderFilterDto(params);

        List<OrderResponseDto> ordersDto = orderService.findAll(orderFilterDto, pageDto);
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
                .toList();
        if (orderItemsDto.isEmpty()) {
            throw new EntityNotFoundException(orderItemId, OrderItemDto.class);
        }

        OrderItemDto orderItemDto = orderItemsDto.get(0);
        orderItemDto.add(orderItemLinksProvider.provide(orderId, orderItemDto));
        return new ResponseEntity<>(orderItemDto, OK);
    }

    @Override
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody @NotNull OrderCreateRequestDto orderCreateRequestDto) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (containsAuthority(auth.getAuthorities(), "ROLE_USER")) {
            UserDto user = userService.findByLogin(auth.getName());
            if (user.getId() != orderCreateRequestDto.getUserId()) {
                throw new AccessException(INVALID_ORDER_USER);
            }
        }

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

    private boolean containsAuthority(Collection<? extends GrantedAuthority> authorities, String providedAuthority) {
        return authorities.stream()
                .map(Object::toString)
                .anyMatch(authorityLine -> authorityLine.equals(providedAuthority));
    }
}
