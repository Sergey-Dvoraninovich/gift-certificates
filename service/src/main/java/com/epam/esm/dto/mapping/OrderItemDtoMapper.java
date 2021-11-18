package com.epam.esm.dto.mapping;

import com.epam.esm.dto.OrderItemDto;
import com.epam.esm.entity.OrderItem;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class OrderItemDtoMapper {
    private final ModelMapper mapper;

    public OrderItem toEntity(OrderItemDto dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, OrderItem.class);
    }

    public OrderItemDto toDto(OrderItem entity) {
        return OrderItemDto.builder()
                .id(entity.getGiftCertificate().getId())
                .price(entity.getPrice())
                .build();
    }
}
