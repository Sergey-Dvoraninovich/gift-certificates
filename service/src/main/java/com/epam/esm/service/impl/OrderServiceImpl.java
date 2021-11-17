package com.epam.esm.service.impl;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.mapping.GiftCertificateDtoMapper;
import com.epam.esm.dto.mapping.OrderDtoMapper;
import com.epam.esm.dto.mapping.OrderItemDtoMapper;
import com.epam.esm.dto.mapping.UserDtoMapper;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.OrderItem;
import com.epam.esm.entity.User;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.InvalidEntityException;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.OrderItemRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.OrderService;
import com.epam.esm.validator.OrderValidator;
import com.epam.esm.validator.ValidationError;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final GiftCertificateRepository giftCertificateRepository;
    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;

    private final OrderDtoMapper orderDtoMapper;
    private final OrderItemDtoMapper oderItemDtoMapper;

    private final OrderValidator orderValidator;

    @Override
    public List<OrderDto> findAll(){
        return orderRepository.findAll()
                .stream()
                .map(orderDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDto findById(long id) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isPresent()) {
            return orderDtoMapper.toDto(order.get());
        }
        else {
            throw new EntityNotFoundException(id, OrderDto.class);
        }
    }

    @Override
    @Transactional
    public OrderDto create(OrderDto orderDto) {
        List<ValidationError> validationErrors = orderValidator.validateWithRequiredParams(orderDto);
        if (!validationErrors.isEmpty()) {
            throw new InvalidEntityException(validationErrors, OrderDto.class);
        }

        List<OrderItem> orderItems = processOrderItems(orderDto);

        long userId = orderDto.getUser().getId();
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new EntityNotFoundException(userId, UserDto.class);
        }

        Order order = orderDtoMapper.toEntity(orderDto);
        order.setOrderItems(orderItems);
        order.setUser(optionalUser.get());
        order = orderRepository.create(order);

        return orderDtoMapper.toDto(order);
    }

    @Override
    @Transactional
    public OrderDto update(OrderDto orderDto) {
        Order order = orderDtoMapper.toEntity(orderDto);
        long id = order.getId();
        Optional<Order> optionalOrder = orderRepository.findById(order.getId());
        if (!optionalOrder.isPresent()) {
            throw new EntityNotFoundException(id, OrderDto.class);
        }
        else {
            order = optionalOrder.get();
        }

        List<OrderItem> orderItems = processOrderItems(orderDto);
        order.setOrderItems(orderItems);
        order = orderRepository.update(order);
        return orderDtoMapper.toDto(order);
    }

    @Override
    @Transactional
    public void delete(long id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (!optionalOrder.isPresent()){
            throw new EntityNotFoundException(id, OrderDto.class);
        }
        orderRepository.delete(optionalOrder.get());
    }

    private List<OrderItem> processOrderItems(OrderDto orderDto) {
        return orderDto.getOrderItems().stream()
                .map(oderItemDtoMapper::toEntity)
                .map(orderItem -> {
                    Optional<OrderItem> optionalOrderItem;
                    optionalOrderItem = orderItemRepository.findById(orderItem.getId());
                    if (!optionalOrderItem.isPresent()) {
                        Optional<GiftCertificate> optionalCertificate;
                        optionalCertificate = giftCertificateRepository.findById(orderItem.getGiftCertificate().getId());
                        if (!optionalCertificate.isPresent()) {
                            throw new EntityNotFoundException(orderItem.getGiftCertificate().getId(), GiftCertificateDto.class);
                        }
                        orderItem.setGiftCertificate(optionalCertificate.get());
                        orderItem.setPrice(optionalCertificate.get().getPrice());
                    }
                    else {
                        orderItem = optionalOrderItem.get();
                    }
                    return orderItem;
                })
                .collect(Collectors.toList());
    }
}
