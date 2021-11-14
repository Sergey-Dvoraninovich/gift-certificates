package com.epam.esm.service;

import com.epam.esm.dto.OrderDto;
import com.sun.org.apache.xpath.internal.operations.Or;

import java.util.List;

public interface OrderService {
    List<OrderDto> findAll();
    OrderDto findById(long id);

    OrderDto create(OrderDto orderDto);
    OrderDto update(OrderDto orderDto);
    void delete(long id);
}
