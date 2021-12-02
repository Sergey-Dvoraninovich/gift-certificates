package com.epam.esm.repository;

import com.epam.esm.TestProfileResolver;
import com.epam.esm.entity.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = TestDatabaseConfig.class)
@ActiveProfiles(resolver = TestProfileResolver.class)
@Transactional
public class OrderRepositoryTestCRUD {
    @Autowired
    private OrderRepository orderRepository;

    @Test
    void testCreate() {
        //Given
        Order order = provideNewOrder();
        Instant date = Instant.now();
        order.setCreateOrderTime(date);
        order.setUpdateOrderTime(date);

        //When
        Order actual = orderRepository.create(order);

        //Then
        assertNotNull(actual);
        assertTrue(actual.getId() > 0);
    }

    @Test
    void testUpdate() {
        //Given
        Order expected = provideNewOrder();
        Instant date = Instant.now();
        expected.setUpdateOrderTime(date);

        //Preparation
        Order storedOrder = provideStoredOrder(expected);

        //When
        Order actual = orderRepository.update(storedOrder);

        //Then
        assertNotNull(actual);
        assertEquals(actual, expected);

        //Clean
        removeRedundantOrder(expected);
    }

    @Test
    void testDelete() {
        //Given
        Order order = provideNewOrder();

        //Preparation
        Order storedOrder = orderRepository.create(order);
        assertNotNull(storedOrder);
        assertTrue(storedOrder.getId() > 0);

        //When
        boolean actual = orderRepository.delete(storedOrder);

        //Then
        assertTrue(actual);
        Optional<Order> deletedOrder = orderRepository.findById(storedOrder.getId());
        assertFalse(deletedOrder.isPresent());
    }

    private Order provideNewOrder() {

        Order order = new Order();
        Instant date = Instant.from(ZonedDateTime.of(2000, 1, 1, 11, 11, 11, 222000000, ZoneId.of("Europe/Minsk")));
        order.setCreateOrderTime(date);
        order.setUpdateOrderTime(date);

        return order;
    }

    private Order provideStoredOrder(Order order) {

        long generatedId = orderRepository.create(order).getId();
        assertTrue(generatedId > 0);

        return order;
    }

    private void removeRedundantOrder(Order order) {
        orderRepository.delete(order);
    }

}
