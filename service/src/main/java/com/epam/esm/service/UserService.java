package com.epam.esm.service;

import com.epam.esm.dto.*;

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
     * Sign up User.
     *
     * @param userSignUpDto the user Sign Up Dto
     * @return the Token Dto
     */
    TokenDto signUp(UserSignUpDto userSignUpDto);

    /**
     * Login User.
     *
     * @param userSignInDto the user Sign In Dto
     * @return the Token Dto
     */
    TokenDto login(UserSignInDto userSignInDto);

    /**
     * Find User by id.
     *
     * @param id the User id
     * @return the User Dto
     */
    UserDto findById(long id);

    /**
     * Find by User by login.
     *
     * @param login the login of User
     * @return the User Dto
     */
    UserDto findByLogin(String login);

    /**
     * Gets user password.
     *
     * @param userDto the User Dto
     * @return the User password
     */
    String getUserPassword(UserDto userDto);

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
