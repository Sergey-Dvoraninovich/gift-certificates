package com.epam.esm.repository;

import com.epam.esm.entity.Order;

import java.util.List;
import java.util.Optional;

/**
 * The interface Order repository.
 */
public interface OrderRepository {
    /**
     * Count amount of all Orders.
     *
     * @return the long amount of Orders
     */
    Long countAll();

    /**
     * Find all Orders.
     *
     * @param sortOrder  the sort order
     * @param pageNumber the page number
     * @param pageSize   the page size
     * @return the list
     */
    List<Order> findAll(OrderingType sortOrder, int pageNumber, int pageSize);

    /**
     * Find Order by id.
     *
     * @param id the id
     * @return the optional Order
     */
    Optional<Order> findById(long id);

    /**
     * Create Order.
     *
     * @param order the Order
     * @return the created Order
     */
    Order create(Order order);

    /**
     * Update Order.
     *
     * @param order the Order
     * @return the updated Order
     */
    Order update(Order order);

    /**
     * Delete Order.
     *
     * @param order the order
     * @return the boolean result of deleting Order
     */
    boolean delete(Order order);
}
