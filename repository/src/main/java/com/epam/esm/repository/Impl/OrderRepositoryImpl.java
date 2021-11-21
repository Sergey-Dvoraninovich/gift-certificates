package com.epam.esm.repository.Impl;

import com.epam.esm.entity.Order;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.OrderingType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.repository.OrderingType.ASC;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {
    private static final String CREATE_ORDER_TIME = "createOrderTime";

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Long countAll() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        query.select(criteriaBuilder.count(query.from(Order.class)));
        return entityManager.createQuery(query).getSingleResult();
    }

    @Override
    public List<Order> findAll(OrderingType sortOrder, int pageNumber, int pageSize) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> query = criteriaBuilder.createQuery(Order.class);
        Root<Order> orderRoot = query.from(Order.class);

        if (sortOrder.equals(ASC)) {
            query.orderBy(criteriaBuilder.asc(orderRoot.get(CREATE_ORDER_TIME)));
        } else {
            query.orderBy(criteriaBuilder.desc(orderRoot.get(CREATE_ORDER_TIME)));
        }

        return entityManager.createQuery(query)
                .setFirstResult((pageNumber - 1) * pageSize)
                .setMaxResults(pageSize)
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
