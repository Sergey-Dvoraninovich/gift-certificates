package com.epam.esm.service;

import com.epam.esm.dto.OrderCreateRequestDto;
import com.epam.esm.dto.OrderResponseDto;
import com.epam.esm.dto.OrderUpdateRequestDto;

import java.util.List;

public interface OrderService {
    List<OrderResponseDto> findAll();
    OrderResponseDto findById(long id);

    OrderResponseDto create(OrderCreateRequestDto orderCreateRequestDto);
    OrderResponseDto update(long id, OrderUpdateRequestDto orderUpdateRequestDto);
    void delete(long id);
}
