package com.epam.esm.service;

import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.UserOrderResponseDto;

import java.util.List;

/**
 * The interface User service.
 */
public interface UserService {
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
    List<UserDto> findAll(Integer pageNumber, Integer pageSize);

    /**
     * Find User by id.
     *
     * @param id the User id
     * @return the User Dto
     */
    UserDto findById(long id);

    /**
     * Count amount of all User orders.
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
     * @return the list of User Orders
     */
    List<UserOrderResponseDto> findUserOrders(long id, Integer pageNumber, Integer pageSize);

    /**
     * Find User Order by User id and Order id.
     *
     * @param userId  the User id
     * @param orderId the Order id
     * @return the User Order Response Dto
     */
    UserOrderResponseDto findUserOrder(long userId, long orderId);
}
