package com.epam.esm.repository;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    List<User> findAll();
    Optional<User> findById(long id);
    List<Order> findUserOrders(long id);
}
