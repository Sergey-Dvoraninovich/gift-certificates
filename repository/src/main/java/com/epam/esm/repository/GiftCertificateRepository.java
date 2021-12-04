package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;

import java.util.List;
import java.util.Optional;

/**
 * The interface Gift certificate repository.
 */
public interface GiftCertificateRepository {
    /**
     * Count amount of all Gift Certificates.
     *
     * @param tagNames               the tag names search param
     * @param certificateName        the certificate name search param
     * @param orderingName           the name ordering type
     * @param certificateDescription the certificate description search param
     * @param orderingCreateDate     the create date ordering type
     * @return the long of Gift Certificates amount
     */
    Long countAll(List<String> tagNames, String certificateName, OrderingType orderingName,
                  String certificateDescription, OrderingType orderingCreateDate);

    /**
     * Find all Gift Certificates.
     *
     * @param tagNames               the tag names search param
     * @param certificateName        the certificate name search param
     * @param orderingName           the name ordering type
     * @param certificateDescription the certificate description search param
     * @param orderingCreateDate     the create date ordering type
     * @param pageNumber             the page number
     * @param pageSize               the page size
     * @return the list of Gift Certificates
     */
    List<GiftCertificate> findAll(List<String> tagNames, String certificateName, OrderingType orderingName,
                                  String certificateDescription, OrderingType orderingCreateDate,
                                  int pageNumber, int pageSize);

    /**
     * Find Gift Certificate by id.
     *
     * @param id the Gift Certificate id
     * @return the optional Gift Certificate
     */
    Optional<GiftCertificate> findById(long id);

    /**
     * Find Gift Certificate by name.
     *
     * @param name the Gift Certificate name
     * @return the optional Gift Certificate
     */
    Optional<GiftCertificate> findByName(String name);


    /**
     * Create Gift Certificate.
     *
     * @param certificate the Gift Certificate
     * @return the created Gift Certificate
     */
    GiftCertificate create(GiftCertificate certificate);

    /**
     * Update Gift Certificate.
     *
     * @param certificate the Gift Certificate
     * @return the updated Gift Certificate
     */
    GiftCertificate update(GiftCertificate certificate);

    /**
     * Delete Gift Certificate.
     *
     * @param certificate the Gift Certificate
     * @return the boolean result of Gift Certificate deletion
     */
    boolean delete(GiftCertificate certificate);
}
