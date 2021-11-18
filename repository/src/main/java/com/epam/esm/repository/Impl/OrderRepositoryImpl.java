package com.epam.esm.repository.Impl;

import com.epam.esm.entity.Order;
import com.epam.esm.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {
    private static final String ALL_ORDERS
            = "SELECT o FROM Order o";

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<Order> findAll() {
        return entityManager.createQuery(ALL_ORDERS, Order.class)
                .getResultList();
    }

    @Override
    public Optional<Order> findById(long id) {
        Order order = entityManager.find(Order.class, id);
        return Optional.ofNullable(order);
    }

    @Override
    public Order create(Order order) {
        entityManager.persist(order);
        return order;
    }

    @Override
    public Order update(Order order) {
        entityManager.merge(order);
        return order;
    }

    @Override
    public boolean delete(Order order) {
        entityManager.remove(order);
        return entityManager.find(Order.class, order.getId()) == null;
    }
}
