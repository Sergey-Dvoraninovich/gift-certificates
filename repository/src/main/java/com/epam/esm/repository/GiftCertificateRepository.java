package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;

import java.util.List;
import java.util.Optional;

/**
 * The interface Gift certificate repository.
 */
public interface GiftCertificateRepository {

    /**
     * Find all GiftCertificates.
     *
     * @param tagName                the name of tag, connected with GiftCertificate
     * @param certificateName        the GiftCertificate name
     * @param orderingName           the ordering type for name
     * @param certificateDescription the GiftCertificate description
     * @param orderingCreateDate     the ordering type for create date
     * @return the list
     */
    List<GiftCertificate> findAll(String tagName, String certificateName, OrderingType orderingName,
                                  String certificateDescription, OrderingType orderingCreateDate);

    /**
     * Find by GiftCertificate id.
     *
     * @param id the id of GiftCertificate
     * @return the optional of GiftCertificate
     */
    Optional<GiftCertificate> findById(long id);

    /**
     * Find by GiftCertificate name.
     *
     * @param name the name of GiftCertificate
     * @return the optional of GiftCertificate
     */
    Optional<GiftCertificate> findByName(String name);

    /**
     * Add Tag to GiftCertificate.
     *
     * @param certificateId the GiftCertificate id
     * @param tagId         the Tag id
     * @return the boolean result of adding
     */
    boolean addCertificateTag(long certificateId, long tagId);

    /**
     * Remove Tag from GiftCertificate.
     *
     * @param certificateId the GiftCertificate id
     * @param tagId         the Tag id
     * @return the boolean result of removing
     */
    boolean removeCertificateTag(long certificateId, long tagId);

    /**
     * Create GiftCertificate.
     *
     * @param certificate the GiftCertificate
     * @return the long id of created GiftCertificate
     */
    long create(GiftCertificate certificate);

    /**
     * Update GiftCertificate.
     *
     * @param certificate the GiftCertificate
     * @return the boolean result of updating
     */
    boolean update(GiftCertificate certificate);

    /**
     * Delete GiftCertificate.
     *
     * @param id the GiftCertificate id
     * @return the boolean result of deleting
     */
    boolean delete(long id);
}
