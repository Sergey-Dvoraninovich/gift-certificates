package com.epam.esm.dto.mapping;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.OrderItemDto;
import com.epam.esm.entity.Order;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderDtoMapper {
    private final ModelMapper mapper;
    private final OrderItemDtoMapper orderItemMapper;

    public Order toEntity(OrderDto dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, Order.class);
    }

    public OrderDto toDto(Order entity) {
        OrderDto dto = Objects.isNull(entity) ? null : mapper.map(entity, OrderDto.class);

        List<OrderItemDto> orderItemsDto = entity.getOrderItems().stream()
                .map(orderItemMapper::toDto)
                .collect(Collectors.toList());

        BigDecimal totalPrice = BigDecimal.ZERO;
        for (OrderItemDto orderItem : orderItemsDto) {
            totalPrice = totalPrice.add(orderItem.getPrice());
        }

        dto.setOrderItems(orderItemsDto);
        dto.setPrice(totalPrice);
        return dto;
    }
}
