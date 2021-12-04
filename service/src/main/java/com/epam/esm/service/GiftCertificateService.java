package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateRequestDto;
import com.epam.esm.dto.GiftCertificateResponseDto;

import java.util.List;

/**
 * The interface Gift certificate service.
 */
public interface GiftCertificateService {
    /**
     * Count amount of all Gift Certificates.
     *
     * @param tagNames               the tag names search param
     * @param certificateName        the certificate name search param
     * @param orderingName           the name ordering type
     * @param certificateDescription the certificate description search param
     * @param orderingCreateDate     the create date ordering type
     * @return the long
     */
    Long countAll(String[] tagNames, String certificateName, String orderingName,
                  String certificateDescription, String orderingCreateDate);

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
     * @return the list
     */
    List<GiftCertificateResponseDto> findAll(String[] tagNames, String certificateName, String orderingName,
                                             String certificateDescription, String orderingCreateDate,
                                             Integer pageNumber, Integer pageSize);

    /**
     * Find Gift Certificate by id.
     *
     * @param id the Gift Certificate id
     * @return the Gift Certificate Response Dto
     */
    GiftCertificateResponseDto findById(long id);

    /**
     * Create Gift Certificate.
     *
     * @param certificate the Gift Certificate Request Dto
     * @return the Gift Certificate Response Dto
     */
    GiftCertificateResponseDto create(GiftCertificateRequestDto certificate);

    /**
     * Update Gift Certificate.
     *
     * @param id             the Gift Certificate id
     * @param certificateDto the Gift Certificate Request Dto
     * @return the Gift Certificate Response Dto
     */
    GiftCertificateResponseDto update(long id, GiftCertificateRequestDto certificateDto);

    /**
     * Delete Gift Certificate.
     *
     * @param id the Gift Certificate id
     */
    void delete(long id);
}
