package com.epam.esm.service;

import com.epam.esm.dto.PageDto;
import com.epam.esm.dto.TokenDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.UserOrderResponseDto;
import com.epam.esm.dto.UserSignInDto;
import com.epam.esm.dto.UserSignUpDto;

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
     * @param pageDto the page dto
     * @return the list
     */
    List<UserDto> findAll(PageDto pageDto);

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
     * @param userId the User id
     * @return the User Dto
     */
    UserDto findById(long userId);

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
     * @param userId the User id
     * @return the long amount of User Orders
     */
    Long countAllUserOrders(long userId);


    /**
     * Find user Orders list.
     *
     * @param userId  the user id
     * @param pageDto the page dto
     * @return the list
     */
    List<UserOrderResponseDto> findUserOrders(long userId, PageDto pageDto);

    /**
     * Find User Order by User id and Order id.
     *
     * @param userId  the User id
     * @param orderId the Order id
     * @return the User Order Response Dto
     */
    UserOrderResponseDto findUserOrder(long userId, long orderId);
}
