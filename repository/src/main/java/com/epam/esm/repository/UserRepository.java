package com.epam.esm.repository;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;

import java.util.List;
import java.util.Optional;

/**
 * The interface User repository.
 */
public interface UserRepository {
    /**
     * Count amount of all Users.
     *
     * @return the long
     */
    Long countAll();

    /**
     * Find all Users.
     *
     * @param pageNumber the page number
     * @param pageSize   the page size
     * @return the list of Users
     */
    List<User> findAll(int pageNumber, int pageSize);

    /**
     * Find User by id.
     *
     * @param id the id
     * @return the optional
     */
    Optional<User> findById(long id);

    /**
     * Count amount of all Use Orders.
     *
     * @param id the User id
     * @return the long amount of User Orders
     */
    Long countAllUserOrders(long id);

    /**
     * Find User Orders.
     *
     * @param id         the User id
     * @param pageNumber the page number
     * @param pageSize   the page size
     * @return the list User Orders
     */
    List<Order> findUserOrders(long id, int pageNumber, int pageSize);
}
