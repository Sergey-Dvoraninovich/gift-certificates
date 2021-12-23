package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateFilterDto;
import com.epam.esm.dto.PageDto;

import java.util.Map;

/**
 * The interface Request service.
 */
public interface RequestService {

    /**
     * Create Page Dto.
     *
     * @param params      the params
     * @param totalAmount the total amount
     * @return the Page Dto
     */
    PageDto createPageDTO(Map<String, Object> params, Long totalAmount);

    /**
     * Create Gift Certificate Filter Dto.
     *
     * @param params the params
     * @return the Gift Certificate Filter Dto
     */
    GiftCertificateFilterDto createGiftCertificateFilterDTO(Map<String, Object> params);
}
