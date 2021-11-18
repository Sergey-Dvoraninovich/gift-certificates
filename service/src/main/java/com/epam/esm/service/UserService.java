package com.epam.esm.service;
;
import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.UserOrderResponseDto;

import java.util.List;

public interface UserService {
    List<UserDto> findAll();
    UserDto findById(long id);
    List<UserOrderResponseDto> findUserOrders(long id);
}
