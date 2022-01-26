package com.epam.esm.repository;

import com.epam.esm.TestProfileResolver;
import com.epam.esm.entity.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestDatabaseConfig.class)
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
        Order actual = orderRepository.save(order);

        //Then
        assertNotNull(actual);
        //System.out.println(actual);
        //assertTrue(actual.getId() > 0);
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
        Order actual = orderRepository.save(storedOrder);

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
        Order storedOrder = orderRepository.save(order);
        assertNotNull(storedOrder);
        //assertTrue(storedOrder.getId() > 0);

        //When
        orderRepository.delete(storedOrder);

        //Then
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

        long generatedId = orderRepository.save(order).getId();
        //assertTrue(generatedId > 0);

        return order;
    }

    private void removeRedundantOrder(Order order) {
        orderRepository.delete(order);
    }

}
