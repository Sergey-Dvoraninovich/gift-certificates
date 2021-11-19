package com.epam.esm.repository;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Long countAll();

    List<User> findAll(int pageNumber, int pageSize);
    Optional<User> findById(long id);

    List<Order> findUserOrders(long id);
    Optional<Tag> findMostWidelyUsedTag(long userId);
}
