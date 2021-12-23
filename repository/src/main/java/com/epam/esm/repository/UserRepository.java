package com.epam.esm.repository;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The interface User repository.
 */
@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long>,
        CrudRepository<User, Long> {
    /**
     * Count amount of all Users.
     *
     * @return the long
     */
    long count();

    /**
     * Find all Users.
     *
     * @param pageable the Pageable
     * @return the list of Users
     */
    Page<User> findAll(Pageable pageable);

    /**
     * Find User by id.
     *
     * @param id the id
     * @return the optional
     */
    Optional<User> findById(long id);

    /**
     * Find User by login optional.
     *
     * @param login the User login
     * @return the optional User login
     */
    Optional<User> findByLogin(String login);

    /**
     * Create or update User.
     *
     * @param user the User
     * @return the created User
     */
    User save(User user);

    /**
     * Count amount of all Use Orders.
     *
     * @param userId the User id
     * @return the long amount of User Orders
     */
    @Query("SELECT COUNT(o) FROM Order o WHERE o.user.id = :userId")
    long countAllUserOrders(@Param("userId") long userId);


    /**
     * Find user orders list.
     *
     * @param userId   the User id
     * @param pageable the pageable
     * @return the list
     */
    @Query("SELECT o FROM Order o WHERE o.user.id = :userId")
    Page<Order> findUserOrders(@Param("userId") long userId, Pageable pageable);
}
