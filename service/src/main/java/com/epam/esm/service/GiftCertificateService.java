package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateRequestDto;
import com.epam.esm.dto.GiftCertificateResponseDto;

import java.util.List;

public interface GiftCertificateService {
    Long countAll(String[] tagNames, String certificateName, String orderingName,
                  String certificateDescription, String orderingCreateDate);
    List<GiftCertificateResponseDto> findAll(String[] tagNames, String certificateName, String orderingName,
                                             String certificateDescription, String orderingCreateDate,
                                             Integer pageNumber, Integer pageSize);

    GiftCertificateResponseDto findById(long id);

    GiftCertificateResponseDto create(GiftCertificateRequestDto certificate);

    GiftCertificateResponseDto update(long id, GiftCertificateRequestDto certificateDto);

    void delete(long id);
}
