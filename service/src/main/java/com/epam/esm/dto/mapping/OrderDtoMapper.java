package com.epam.esm.dto.mapping;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.Order;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderDtoMapper {
    private final ModelMapper mapper;
    private final GiftCertificateDtoMapper giftCertificateDtoMapper;

    public Order toEntity(OrderDto dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, Order.class);
    }

    public OrderDto toDto(Order entity) {
        OrderDto dto = Objects.isNull(entity) ? null : mapper.map(entity, OrderDto.class);
        List<GiftCertificateDto> certificatesDto = entity.getOrderGiftCertificates().stream()
                .map(giftCertificateDtoMapper::toDto)
                .collect(Collectors.toList());
        dto.setOrderGiftCertificates(certificatesDto);
        return dto;
    }
}
