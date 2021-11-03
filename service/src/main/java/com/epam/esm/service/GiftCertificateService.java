package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateDto;

import java.util.List;

/**
 * The interface Gift certificate service.
 */
public interface GiftCertificateService {
    /**
     * Find all.
     *
     * @param tagName                the name of connected tag
     * @param certificateName        the certificate name
     * @param orderingName           the ordering type for name
     * @param certificateDescription the certificate description
     * @param orderingCreateDate     the ordering type for create date
     * @return the list of GiftCertificateDto
     */
    List<GiftCertificateDto> findAll(String tagName, String certificateName, String orderingName,
                                     String certificateDescription, String orderingCreateDate);

    /**
     * Find by GiftCertificateDto id.
     *
     * @param id the id of GiftCertificateDto
     * @return the stored GiftCertificateDto
     */
    GiftCertificateDto findById(long id);

    /**
     * Create GiftCertificateDto.
     *
     * @param certificate the GiftCertificateDto
     * @return the created GiftCertificateDto
     */
    GiftCertificateDto create(GiftCertificateDto certificate);

    /**
     * Update GiftCertificateDto.
     *
     * @param certificate the GiftCertificateDto for partial
     * @return the patched GiftCertificateDto
     */
    GiftCertificateDto update(GiftCertificateDto certificate);

    /**
     * Delete GiftCertificateDto.
     *
     * @param id the GiftCertificateDto id
     */
    void delete(long id);
}
