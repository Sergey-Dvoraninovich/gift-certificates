package com.epam.esm.dto.mapping;

import com.epam.esm.dto.OrderItemDto;
import com.epam.esm.dto.UserOrderResponseDto;
import com.epam.esm.entity.Order;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class UserOrderResponseDtoMapper {
    private final ModelMapper mapper;
    private final OrderItemDtoMapper orderItemMapper;

    public Order toEntity(UserOrderResponseDto dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, Order.class);
    }

    public UserOrderResponseDto toDto(Order entity) {
        UserOrderResponseDto dto = null;

        if (!Objects.isNull(entity)) {
            dto = mapper.map(entity, UserOrderResponseDto.class);

            List<OrderItemDto> orderItemsDto = entity.getOrderItems() == null
                    ? null
                    : entity.getOrderItems().stream()
                    .map(orderItemMapper::toDto)
                    .toList();

            BigDecimal totalPrice = BigDecimal.ZERO;
            int count = 0;
            for (OrderItemDto orderItem : orderItemsDto) {
                totalPrice = totalPrice.add(orderItem.getPrice());
                count++;
            }

            dto.setTotalPrice(totalPrice);
            dto.setCount(count);
        }
        return dto;
    }
}