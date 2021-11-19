package com.epam.esm.service;
;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.UserOrderResponseDto;

import java.util.List;

public interface UserService {
    Long countAll();
    List<UserDto> findAll(Integer pageNumber, Integer pageSize);
    UserDto findById(long id);

    List<UserOrderResponseDto> findUserOrders(long id);
    TagDto findMostWidelyUsedTag(long userId);
}
