package com.epam.esm.repository;

import com.epam.esm.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    Long countAll();

    List<Order> findAll(OrderingType sortOrder, int pageNumber, int pageSize);
    Optional<Order> findById(long id);

    Order create(Order order);
    Order update(Order order);
    boolean delete(Order order);
}
