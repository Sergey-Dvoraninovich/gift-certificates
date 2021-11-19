package com.epam.esm.repository.Impl;

import com.epam.esm.entity.*;
import com.epam.esm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private static final String USER_ID_PARAM = "userId";

    private static final String COUNT_USERS
            = "SELECT COUNT(u) FROM User u";

    private static final String ALL_USERS
            = "SELECT u FROM User u";

    private static final String USER_ORDERS
            = "SELECT o FROM Order o WHERE o.user.id = :userId";

    private static final String FIND_MOST_WIDELY_USED_USER_TAG
            = "SELECT t FROM Order o "
            + "JOIN o.user u "
            + "JOIN o.orderItems i "
            + "JOIN i.giftCertificate c "
            + "JOIN c.giftCertificateTags t "
            + "WHERE u.id = :userId "
            + "GROUP BY t";

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Long countAll() {
        return (Long) entityManager.createQuery(COUNT_USERS)
                .getSingleResult();
    }

    @Override
    public List<User> findAll(int pageNumber, int pageSize) {
        return entityManager.createQuery(ALL_USERS, User.class)
                .setFirstResult((pageNumber - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    @Override
    public Optional<User> findById(long id) {
        User user = entityManager.find(User.class, id);
        return Optional.ofNullable(user);
    }

    @Override
    public List<Order> findUserOrders(long id) {
        return entityManager.createQuery(USER_ORDERS, Order.class)
                .setParameter(USER_ID_PARAM, id)
                .getResultList();
    }

    @Override
    public Optional<Tag> findMostWidelyUsedTag(long userId) {
        List<Tag> tags = entityManager.createQuery(FIND_MOST_WIDELY_USED_USER_TAG, Tag.class)
                .setParameter(USER_ID_PARAM, userId)
                .getResultList();
        for (Tag tag: tags) {
            System.out.println(tag);
        }

        return Optional.empty();
//        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
//        CriteriaQuery<Tag> query = criteriaBuilder.createQuery(Tag.class);
//
//        Root<User> userRoot = query.from(User.class);
//        Root<Order> orderRoot = query.from(Order.class);
//        Root<OrderItem> orderItemRoot = query.from(OrderItem.class);
//        Root<GiftCertificate> certificateRoot = query.from(GiftCertificate.class);
//        Root<Tag> tagRoot = query.from(Tag.class);
//
//        Join<Order, User> orderUserJoin = orderRoot.join();
//        Join<Order, OrderItem> orderOrderItemJoin = orderRoot.join("orderItems");
//        Join<OrderItem, GiftCertificate> orderItemGiftCertificateJoin = orderItemRoot.join("giftCertificate");
//        Join<GiftCertificate, Tag> giftCertificateTagJoin = certificateRoot.join("giftCertificateTags");
//
//        query.select(tagRoot).where(criteriaBuilder.equal(
//                ., userId));
//
//        List<Tag> tags = entityManager.createQuery(query).getResultList();
//        for (Tag tag: tags) {
//            System.out.println(tag);
//        }
//
//        return Optional.empty();
    }
}
