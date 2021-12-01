package com.epam.esm.repository.Impl;

import com.epam.esm.entity.OrderItem;
import com.epam.esm.repository.OrderItemRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class OrderItemRepositoryImpl implements OrderItemRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<OrderItem> findById(long id) {
        OrderItem orderItem = entityManager.find(OrderItem.class, id);
        return Optional.ofNullable(orderItem);
    }

    @Override
    public OrderItem create(OrderItem orderItem) {
        entityManager.persist(orderItem);
        return orderItem;
    }

    @Override
    public OrderItem update(OrderItem orderItem) {
        entityManager.persist(orderItem);
        return orderItem;
    }

    @Override
    public boolean delete(OrderItem orderItem) {
        entityManager.remove(orderItem);
        return entityManager.find(OrderItem.class, orderItem.getId()) == null;
    }
}
