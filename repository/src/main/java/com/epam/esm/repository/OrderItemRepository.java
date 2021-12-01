package com.epam.esm.repository;

import com.epam.esm.entity.OrderItem;

import java.util.Optional;

//TODO remove after tests
public interface OrderItemRepository {
    Optional<OrderItem> findById(long id);

    OrderItem create(OrderItem orderItem);
    OrderItem update(OrderItem orderItem);
    boolean delete(OrderItem orderItem);
}
