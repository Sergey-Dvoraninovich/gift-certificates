package com.epam.esm.service.impl;

import com.epam.esm.dto.OrderResponseDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.UserOrderResponseDto;
import com.epam.esm.dto.mapping.OrderResponseDtoMapper;
import com.epam.esm.dto.mapping.UserDtoMapper;
import com.epam.esm.dto.mapping.UserOrderResponseDtoMapper;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserDtoMapper userMapper;
    private final OrderResponseDtoMapper orderResponseDtoMapper;
    private final UserOrderResponseDtoMapper userOrderResponseDtoMapper;

    @Override
    public List<UserDto> findAll(){
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findById(long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return userMapper.toDto(user.get());
        }
        else {
            throw new EntityNotFoundException(id, UserDto.class);
        }
    }

    @Override
    public List<UserOrderResponseDto> findUserOrders(long id) {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) {
            throw new EntityNotFoundException(id, UserDto.class);
        }
        List<Order> orders = userRepository.findUserOrders(id);
        return orders.stream()
                .map(userOrderResponseDtoMapper::toDto)
                .collect(Collectors.toList());
    }
}
