package com.epam.esm.service.impl;

import com.epam.esm.dto.OrderCreateRequestDto;
import com.epam.esm.dto.OrderItemDto;
import com.epam.esm.dto.OrderResponseDto;
import com.epam.esm.dto.OrderUpdateRequestDto;
import com.epam.esm.dto.PageDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.mapping.OrderResponseDtoMapper;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.OrderItem;
import com.epam.esm.entity.User;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.InvalidEntityException;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.OrderService;
import com.epam.esm.validator.OrderCreateValidator;
import com.epam.esm.validator.OrderSearchParamsValidator;
import com.epam.esm.validator.OrderUpdateValidator;
import com.epam.esm.validator.ValidationError;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final GiftCertificateRepository giftCertificateRepository;
    private final UserRepository userRepository;

    private final OrderResponseDtoMapper orderDtoMapper;

    private final OrderCreateValidator orderCreateValidator;
    private final OrderUpdateValidator orderUpdateValidator;
    private final OrderSearchParamsValidator orderSearchParamsValidator;

    @Override
    public Long countAll() {
        return orderRepository.count();
    }

    @Override
    public List<OrderResponseDto> findAll(PageDto pageDto){

        return orderRepository.findAll(pageDto.toPageable())
                .stream()
                .map(orderDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponseDto findById(long id) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isPresent()) {
            return orderDtoMapper.toDto(order.get());
        }
        else {
            throw new EntityNotFoundException(id, OrderResponseDto.class);
        }
    }

    @Override
    @Transactional
    public OrderResponseDto create(OrderCreateRequestDto orderCreateRequestDto) {
        List<ValidationError> validationErrors = orderCreateValidator.validateWithRequiredParams(orderCreateRequestDto);
        if (!validationErrors.isEmpty()) {
            throw new InvalidEntityException(validationErrors, OrderCreateRequestDto.class);
        }

        List<OrderItem> orderItems = orderCreateRequestDto.getOrderGiftCertificates().stream()
                .map(this::createNewOrderItem)
                .collect(Collectors.toList());

        long userId = orderCreateRequestDto.getUserId();
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new EntityNotFoundException(userId, UserDto.class);
        }

        Order order = new Order();
        order.setCreateOrderTime(Instant.now());
        order.setUpdateOrderTime(Instant.now());
        order.setOrderItems(orderItems);
        order.setUser(optionalUser.get());

        order = orderRepository.save(order);
        return orderDtoMapper.toDto(order);
    }

    @Override
    @Transactional
    public OrderResponseDto update(long id, OrderUpdateRequestDto orderUpdateRequestDto) {
        List<ValidationError> validationErrors = orderUpdateValidator.validateWithRequiredParams(orderUpdateRequestDto);
        if (!validationErrors.isEmpty()) {
            throw new InvalidEntityException(validationErrors, OrderCreateRequestDto.class);
        }

        Order order;
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (!optionalOrder.isPresent()) {
            throw new EntityNotFoundException(id, OrderUpdateRequestDto.class);
        }
        else {
            order = optionalOrder.get();
        }

        List<OrderItem> orderItems = processOrderItems(orderUpdateRequestDto.getOrderGiftCertificates(),
                                                       optionalOrder.get().getOrderItems());
        order.setUpdateOrderTime(Instant.now());
        order.setOrderItems(orderItems);

        order = orderRepository.save(order);
        return orderDtoMapper.toDto(order);
    }

    @Override
    @Transactional
    public void delete(long id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (!optionalOrder.isPresent()){
            throw new EntityNotFoundException(id, OrderResponseDto.class);
        }
        orderRepository.delete(optionalOrder.get());
    }

    private OrderItem createNewOrderItem(OrderItemDto orderItemDto) {
        OrderItem orderItem = new OrderItem();
        Optional<GiftCertificate> optionalCertificate;
        optionalCertificate = giftCertificateRepository.findById(orderItemDto.getId());
        if (!optionalCertificate.isPresent()) {
            throw new EntityNotFoundException(orderItemDto.getId(), OrderItemDto.class);
        }
        else {
            orderItem.setGiftCertificate(optionalCertificate.get());
            orderItem.setPrice(optionalCertificate.get().getPrice());
        }
        return orderItem;
    }

    private List<OrderItem> processOrderItems(List<OrderItemDto> orderItemsDto, List<OrderItem> orderItems) {
        return orderItemsDto.stream()
                .map(orderItemDto -> {
                    final long orderItemId = orderItemDto.getId();
                    List<OrderItem> suitableStoredItems = orderItems.stream()
                            .filter(storedOrderItem -> storedOrderItem.getGiftCertificate().getId() == orderItemId)
                            .collect(Collectors.toList());

                    return suitableStoredItems.size() == 1
                            ? suitableStoredItems.get(0)
                            : createNewOrderItem(orderItemDto);
                })
                .collect(Collectors.toList());
    }
}
