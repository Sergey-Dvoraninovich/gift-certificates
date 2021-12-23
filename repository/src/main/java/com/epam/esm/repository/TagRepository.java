package com.epam.esm.repository;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * The interface Tag repository.
 */
@Repository
public interface TagRepository extends PagingAndSortingRepository<Tag, Long>,
        CrudRepository<Tag, Long> {
    /**
     * Count all Tags amount.
     *
     * @return the long amount of Tags
     */
    long count();

    /**
     * Find all Tags.
     *
     * @param pageable the Pageable
     * @return the list of Tags
     */
    Page<Tag> findAll(Pageable pageable);

    /**
     * Find all Tags.
     *
     * @param specification the specification
     * @return the list of Tags
     */
    List<Tag> findAll(Specification<Tag> specification);

    /**
     * Find Tag by id.
     *
     * @param id the id of Tag
     * @return the optional Tag
     */
    Optional<Tag> findById(Long id);

    /**
     * Find Tag by name.
     *
     * @param name the Tag name
     * @return the optional Tag
     */
    Optional<Tag> findTagByName(String name);

    /**
     * Create Tag.
     *
     * @param tag the Tag
     * @return the Tag
     */
    Tag save(Tag tag);

    /**
     * Delete Tag.
     *
     * @param tag the Tag
     */
    void delete(Tag tag);

    /**
     * Find Orders highest coast.
     *
     * @return the list
     */
    @Query( "SELECT o FROM Order o "
            + "JOIN o.user u "
            + "JOIN o.orderItems i "
            + "GROUP BY o "
            + "ORDER BY SUM(i.price) DESC")
    List<Order> findOrdersHighestCoast();

    /**
     * Find most widely used User tags list.
     *
     * @param userId the User id
     * @return the list
     */
    @Query( "SELECT t FROM Order o "
            + "JOIN o.user u "
            + "JOIN o.orderItems i "
            + "JOIN i.giftCertificate c "
            + "JOIN c.giftCertificateTags t "
            + "WHERE u.id = :userId "
            + "GROUP BY t")
    List<Tag> findMostWidelyUsedUserTags(@Param("userId") long userId);
}
