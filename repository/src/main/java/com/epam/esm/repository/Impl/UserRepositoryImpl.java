package com.epam.esm.repository.Impl;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private static final String USER_ID_PARAM = "userId";

    private static final String ALL_USERS
            = "SELECT u FROM User u";

    private static final String USER_ORDERS
            = "SELECT o FROM Order o WHERE o.user.id = :userId";

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<User> findAll() {
        return entityManager.createQuery(ALL_USERS, User.class)
                .getResultList();
    }

    @Override
    public Optional<User> findById(long id) {
        User user = entityManager.find(User.class, id);
        return Optional.ofNullable(user);
    }

    @Override
    public List<Order> findUserOrders(long id) {
        return entityManager.createQuery(USER_ORDERS, Order.class)
                .setParameter(USER_ID_PARAM, id)
                .getResultList();
    }
}
