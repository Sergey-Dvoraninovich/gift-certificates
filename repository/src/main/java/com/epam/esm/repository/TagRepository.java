package com.epam.esm.repository;

import com.epam.esm.entity.Tag;

import java.util.List;
import java.util.Optional;

/**
 * The interface Tag repository.
 */
public interface TagRepository {

    /**
     * Find all Tags.
     *
     * @return the list of Tag
     */
    List<Tag> findAll();

    /**
     * Find Tag by id.
     *
     * @param id the Tag id
     * @return the optional of Tag
     */
    Optional<Tag> findById(long id);

    /**
     * Find Tag by name.
     *
     * @param name the Tag name
     * @return the optional of Tag
     */
    Optional<Tag> findByName(String name);

    /**
     * Find Tag by GiftCertificate id.
     *
     * @param certificateId the GiftCertificate id
     * @return the list of Tags
     */
    List<Tag> findByCertificateId(long certificateId);

    /**
     * Create Tag.
     *
     * @param tag the Tag
     * @return the long id of Tag
     */
    long create(Tag tag);

    /**
     * Delete Tag.
     *
     * @param id the Tag id
     * @return the boolean result of deleting
     */
    boolean delete(long id);
}
