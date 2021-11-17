package com.epam.esm.repository;

import com.epam.esm.entity.OrderItem;
import com.epam.esm.entity.Tag;

import java.util.Optional;

public interface OrderItemRepository {
    Optional<OrderItem> findById(long id);

    OrderItem create(OrderItem orderItem);
    OrderItem update(OrderItem orderItem);
    boolean delete(OrderItem orderItem);
}
