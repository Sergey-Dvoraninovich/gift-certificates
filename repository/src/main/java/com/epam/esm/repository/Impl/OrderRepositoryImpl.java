package com.epam.esm.repository.Impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.time.Instant;
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
        order.setOrderTime(Instant.now());
        BigDecimal totalPrice = new BigDecimal("0.00");
        for (GiftCertificate certificate: order.getOrderGiftCertificates()) {
            totalPrice = totalPrice.add(certificate.getPrice());
        }
        order.setPrice(totalPrice);

        entityManager.persist(order);
        return order;
    }

    @Override
    public Order update(Order order) {
        order.setOrderTime(Instant.now());
        BigDecimal totalPrice = new BigDecimal("0.00");
        for (GiftCertificate certificate: order.getOrderGiftCertificates()) {
            totalPrice = totalPrice.add(certificate.getPrice());
        }
        order.setPrice(totalPrice);

        entityManager.merge(order);
        return order;
    }

    @Override
    public boolean delete(Order order) {
        entityManager.remove(order);
        return entityManager.find(Order.class, order.getId()) == null;
    }
}
