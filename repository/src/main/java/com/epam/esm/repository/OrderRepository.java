package com.epam.esm.repository;

import com.epam.esm.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The interface Order repository.
 */
@Repository
public interface OrderRepository extends PagingAndSortingRepository<Order, Long> {
    /**
     * Count amount of all Orders.
     *
     * @return the long amount of Orders
     */
    long count();

    Page<Order> findAll(Pageable pageable);

    /**
     * Find Order by id.
     *
     * @param id the id
     * @return the optional Order
     */
    Optional<Order> findById(long id);

    /**
     * Create or update Order.
     *
     * @param order the Order
     * @return the created Order
     */
    Order save(Order order);

    /**
     * Delete Order.
     *
     * @param order the order
     */
    void delete(Order order);
}
