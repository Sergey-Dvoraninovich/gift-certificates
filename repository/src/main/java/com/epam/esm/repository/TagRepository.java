package com.epam.esm.repository;

import com.epam.esm.entity.Tag;

import java.util.List;
import java.util.Optional;

/**
 * The interface Tag repository.
 */
public interface TagRepository {
    /**
     * Count all Tags amount.
     *
     * @return the long amount of Tags
     */
    Long countAll();

    /**
     * Find all Tags.
     *
     * @param pageNumber the page number
     * @param pageSize   the page size
     * @return the list of Tags
     */
    List<Tag> findAll(int pageNumber, int pageSize);

    /**
     * Find Tag by id.
     *
     * @param id the id of Tag
     * @return the optional Tag
     */
    Optional<Tag> findById(long id);

    /**
     * Find Tag by name.
     *
     * @param name the Tag name
     * @return the optional Tag
     */
    Optional<Tag> findByName(String name);

    /**
     * Create Tag.
     *
     * @param tag the Tag
     * @return the Tag
     */
    Tag create(Tag tag);

    /**
     * Delete Tag.
     *
     * @param tag the Tag
     * @return the boolean result of Tag deletion
     */
    boolean delete(Tag tag);

    /**
     * Find most widely used Tag of User with highest order coast
     *
     * @return the optional Tag
     */
    Optional<Tag> findMostWidelyUsedTag();
}
