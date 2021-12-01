package com.epam.esm.repository.Impl;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.repository.UserRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private static final String USER_ID_PARAM = "userId";

    private static final String COUNT_ALL_USERS
            = "SELECT COUNT(u) FROM User u";

    private static final String ALL_USERS
            = "SELECT u FROM User u";

    private static final String COUNT_USER_ORDERS
            = "SELECT COUNT(o) FROM Order o WHERE o.user.id = :userId";

    private static final String USER_ORDERS
            = "SELECT o FROM Order o WHERE o.user.id = :userId";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long countAll() {
        return (Long) entityManager.createQuery(COUNT_ALL_USERS)
                .getSingleResult();
    }

    @Override
    public List<User> findAll(int pageNumber, int pageSize) {
        return entityManager.createQuery(ALL_USERS, User.class)
                .setFirstResult((pageNumber - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    @Override
    public Optional<User> findById(long id) {
        User user = entityManager.find(User.class, id);
        return Optional.ofNullable(user);
    }

    @Override
    public Long countAllUserOrders(long id) {
        return (Long) entityManager.createQuery(COUNT_USER_ORDERS)
                .setParameter(USER_ID_PARAM, id)
                .getSingleResult();
    }

    @Override
    public List<Order> findUserOrders(long id, int pageNumber, int pageSize) {
        return entityManager.createQuery(USER_ORDERS, Order.class)
                .setParameter(USER_ID_PARAM, id)
                .setFirstResult((pageNumber - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }
}
