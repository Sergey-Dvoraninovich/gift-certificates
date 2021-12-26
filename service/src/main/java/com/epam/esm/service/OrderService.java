package com.epam.esm.service;

import com.epam.esm.dto.*;

import java.util.List;

/**
 * The interface Order service.
 */
public interface OrderService {
    /**
     * Count amount of all Order.
     *
     * @return the long
     */
    Long countAll();

    List<OrderResponseDto> findAll(OrderFilterDto orderFilterDto, PageDto pageDto);

    /**
     * Find Order by id.
     *
     * @param id the Order id
     * @return the Order Response Dto
     */
    OrderResponseDto findById(long id);

    /**
     * Create Order.
     *
     * @param orderCreateRequestDto the Order Create Request Dto
     * @return the Order Response Dto
     */
    OrderResponseDto create(OrderCreateRequestDto orderCreateRequestDto);

    /**
     * Update Order.
     *
     * @param id                    the Order id
     * @param orderUpdateRequestDto the Order Update Request Dto
     * @return the order response dto
     */
    OrderResponseDto update(long id, OrderUpdateRequestDto orderUpdateRequestDto);

    /**
     * Delete Order.
     *
     * @param id the Order id
     */
    void delete(long id);
}
