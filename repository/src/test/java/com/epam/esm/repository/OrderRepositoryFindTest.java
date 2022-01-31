package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.OrderItem;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.entity.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.entity.UserRoleName.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class OrderRepositoryFindTest {
    private static final Integer PAGE_NUMBER = 0;
    private static final Integer PAGE_SIZE = 10;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void testCountAll() {
        //Given
        List<Order> expected = provideOrdersList();

        //When
        long actual = orderRepository.count();

        //Then
        assertEquals(expected.size(), actual);
    }

    @Test
    void testFindAll() {
        //Given
        List<Order> expected = provideOrdersList();

        //When
        Page<Order> page = orderRepository.findAll(PageRequest.of(PAGE_NUMBER, PAGE_SIZE));

        //Then
        List<Order> actual = page.stream().toList();
        assertEquals(expected.size(), actual.size());
    }

    @Test
    void testFindById() {
        //Given
        Order expectedOrder = provideOrdersList().get(0);

        //When
        Optional<Order> order = orderRepository.findById(expectedOrder.getId());

        //Then
        assertTrue(order.isPresent());
        assertEquals(expectedOrder.getId(), order.get().getId());
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
        secondOrder.setOrderItems(List.of(orderItems.get(2)));
        secondOrder.setUser(users.get(0));
        date = Instant.from(ZonedDateTime.of(2011, 1, 1, 11, 11, 11, 222000000, ZoneId.of("Europe/Minsk")));
        secondOrder.setCreateOrderTime(date);
        secondOrder.setUpdateOrderTime(date);

        Order thirdOrder = new Order();
        thirdOrder.setId(3L);
        thirdOrder.setOrderItems(Arrays.asList(orderItems.get(3), orderItems.get(4)));
        thirdOrder.setUser(users.get(1));
        date = Instant.from(ZonedDateTime.of(2022, 1, 1, 11, 11, 11, 222000000, ZoneId.of("Europe/Minsk")));
        thirdOrder.setCreateOrderTime(date);
        thirdOrder.setUpdateOrderTime(date);

        return Arrays.asList(firstOrder, secondOrder, thirdOrder);
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

        orderItem = new OrderItem();
        orderItem.setId(4L);
        orderItem.setGiftCertificate(provideCertificateWithoutTag());
        orderItem.setPrice(new BigDecimal("192.92"));
        orderItemList.add(orderItem);

        orderItem = new OrderItem();
        orderItem.setId(5L);
        orderItem.setGiftCertificate(provideSingleTagCertificate());
        orderItem.setPrice(new BigDecimal("67.92"));
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

    private GiftCertificate provideCertificateWithoutTag() {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(3L);
        giftCertificate.setName("certificate");
        giftCertificate.setDescription("certificate");
        giftCertificate.setPrice(new BigDecimal("200.00"));
        giftCertificate.setDuration(Duration.ofDays(180));
        Instant date = Instant.from(ZonedDateTime.of(2022, 1, 1, 11, 11, 11, 222000000, ZoneId.of("Europe/Minsk")));
        giftCertificate.setCreateDate(date);
        giftCertificate.setLastUpdateDate(date);
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

    private List<User> provideUsersList() {
        User firstUser = new User();
        firstUser.setId(1L);
        firstUser.setLogin("christian_altman");
        firstUser.setPassword("a0f3285b07c26c0dcd2191447f391170d06035e8d57e31a048ba87074f3a9a15");
        firstUser.setRole(provideRole());
        firstUser.setName("Christian");
        firstUser.setSurname("Altman");
        firstUser.setEmail("christian.altman@gmail.com");

        User secondUser = new User();
        secondUser.setId(2L);
        secondUser.setLogin("cindy_clark");
        secondUser.setPassword("a0f3285b07c26c0dcd2191447f391170d06035e8d57e31a048ba87074f3a9a15");
        secondUser.setRole(provideRole());
        secondUser.setName("Cindy");
        secondUser.setSurname("Clark");
        secondUser.setEmail("cindy.clark@gmail.com");

        return Arrays.asList(firstUser, secondUser);
    }

    private UserRole provideRole() {
        UserRole role = new UserRole();
        role.setId(1);
        role.setName(USER);
        return role;
    }
}
