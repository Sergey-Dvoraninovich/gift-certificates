package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateFilterDto;
import com.epam.esm.dto.GiftCertificateRequestDto;
import com.epam.esm.dto.GiftCertificateResponseDto;
import com.epam.esm.dto.PageDto;

import java.util.List;

/**
 * The interface Gift certificate service.
 */
public interface GiftCertificateService {

    Long countAll(GiftCertificateFilterDto filterDto);

    List<GiftCertificateResponseDto> findAll(GiftCertificateFilterDto filterDto, PageDto pageDto);

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
