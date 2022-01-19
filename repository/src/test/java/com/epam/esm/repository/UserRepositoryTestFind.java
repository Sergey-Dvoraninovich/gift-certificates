package com.epam.esm.repository;

import com.epam.esm.TestProfileResolver;
import com.epam.esm.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest(classes = TestDatabaseConfig.class)
@ActiveProfiles(resolver = TestProfileResolver.class)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {"classpath:init_data.sql"})
public class UserRepositoryTestFind {
    private static final Integer PAGE_NUMBER = 0;
    private static final Integer PAGE_SIZE = 10;

    @Autowired
    private UserRepository userRepository;

//    @Test
//    void testCountAll() {
//        List<User> expected = provideUsersList();
//
//        List<User> actual;
//        actual = (List<User>) userRepository.findAll(PageRequest.of(PAGE_NUMBER, PAGE_SIZE));
//
//        assertEquals(expected.size(), actual.size());
//    }
//
//    @Test
//    void testFindAll() {
//        List<User> expected = provideUsersList();
//
//        List<User> actual;
//        actual = (List<User>) userRepository.findAll(PageRequest.of(PAGE_NUMBER, PAGE_SIZE));
//
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    void testFindById() {
//        User expectedUser = provideUsersList().get(0);
//        Optional<User> user = userRepository.findById(expectedUser.getId());
//        boolean result = false;
//        if (user.isPresent()){
//            result = user.get().getId() == expectedUser.getId();
//        }
//        assertTrue(result);
//    }
//
//    @Test
//    void testCountAllUserOrders() {
//        List<Order> userOrders = provideOrdersList();
//        User user = provideUsersList().get(0);
//
//        long actual = userRepository.countAllUserOrders(user.getId());
//
//        assertEquals(userOrders.size(), actual);
//    }
//
//    @Test
//    void testFindAllUserOrders() {
//        List<Order> userOrders = provideOrdersList();
//        User user = provideUsersList().get(0);
//
//        List<Order> actual;
//        actual = (List<Order>) userRepository.findUserOrders(user.getId(), PageRequest.of(PAGE_NUMBER, PAGE_SIZE));
//
//        assertEquals(userOrders, actual);
//    }


    private List<User> provideUsersList() {
        User firstUser = new User();
        firstUser.setId(1L);
        firstUser.setLogin("christian_altman");
        firstUser.setName("Christian");
        firstUser.setSurname("Altman");
        firstUser.setEmail("christian.altman@gmail.com");

        User secondUser = new User();
        secondUser.setId(2L);
        secondUser.setLogin("cindy_clark");
        secondUser.setName("Cindy");
        secondUser.setSurname("Clark");
        secondUser.setEmail("cindy.clark@gmail.com");

        return Arrays.asList(firstUser, secondUser);
    }

    private List<Order> provideOrdersList() {
        Instant date;
        List<OrderItem> orderItems = provideOrderItems();
        List<User> users = provideUsersList();

        Order firstOrder = new Order();
        firstOrder.setId(1L);
        firstOrder.setOrderItems(Arrays.asList(orderItems.get(0), orderItems.get(1)));
        firstOrder.setUser(users.get(0));
        date = Instant.from(ZonedDateTime.of(2000, 1, 1, 11, 11, 11, 222000000, ZoneId.of("Europe/Minsk")));
        firstOrder.setCreateOrderTime(date);
        firstOrder.setUpdateOrderTime(date);

        Order secondOrder = new Order();
        secondOrder.setId(2L);
        secondOrder.setOrderItems(Arrays.asList(orderItems.get(2)));
        secondOrder.setUser(users.get(0));
        date = Instant.from(ZonedDateTime.of(2011, 1, 1, 11, 11, 11, 222000000, ZoneId.of("Europe/Minsk")));
        secondOrder.setCreateOrderTime(date);
        secondOrder.setUpdateOrderTime(date);

        return Arrays.asList(firstOrder, secondOrder);
    }

    private List<OrderItem> provideOrderItems() {
        List<OrderItem> orderItemList = new ArrayList<>();
        OrderItem orderItem;

        orderItem = new OrderItem();
        orderItem.setId(1L);
        orderItem.setGiftCertificate(provideMultipleTagsGiftCertificate());
        orderItem.setPrice(new BigDecimal("72.92"));
        orderItemList.add(orderItem);

        orderItem = new OrderItem();
        orderItem.setId(2L);
        orderItem.setGiftCertificate(provideSingleTagCertificate());
        orderItem.setPrice(new BigDecimal("392.92"));
        orderItemList.add(orderItem);

        orderItem = new OrderItem();
        orderItem.setId(3L);
        orderItem.setGiftCertificate(provideSingleTagCertificate());
        orderItem.setPrice(new BigDecimal("92.92"));
        orderItemList.add(orderItem);

        return orderItemList;
    }

    private GiftCertificate provideMultipleTagsGiftCertificate() {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(1L);
        giftCertificate.setName("certificate first and second tags");
        giftCertificate.setDescription("certificate with first tag and second tags");
        giftCertificate.setPrice(new BigDecimal("50.00"));
        giftCertificate.setDuration(Duration.ofDays(90));
        Instant date = Instant.from(ZonedDateTime.of(2000, 1, 1, 11, 11, 11, 222000000, ZoneId.of("Europe/Minsk")));
        giftCertificate.setCreateDate(date);
        giftCertificate.setLastUpdateDate(date);
        giftCertificate.setGiftCertificateTags(provideTagsList());
        return giftCertificate;
    }

    private GiftCertificate provideSingleTagCertificate() {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(2L);
        giftCertificate.setName("certificate second tag");
        giftCertificate.setDescription("certificate with second tag");
        giftCertificate.setPrice(new BigDecimal("100.00"));
        giftCertificate.setDuration(Duration.ofDays(180));
        Instant date = Instant.from(ZonedDateTime.of(2011, 1, 1, 11, 11, 11, 222000000, ZoneId.of("Europe/Minsk")));
        giftCertificate.setCreateDate(date);
        giftCertificate.setLastUpdateDate(date);
        giftCertificate.setGiftCertificateTags(Arrays.asList(provideTagsList().get(1)));
        return giftCertificate;
    }

    private List<Tag> provideTagsList() {
        Tag firstTag = new Tag();
        firstTag.setId(1L);
        firstTag.setName("first tag");

        Tag secondTag = new Tag();
        secondTag.setId(2L);
        secondTag.setName("second tag");

        return Arrays.asList(firstTag, secondTag);
    }
}
