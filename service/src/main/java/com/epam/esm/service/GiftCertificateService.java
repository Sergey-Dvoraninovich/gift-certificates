package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateDto;

import java.util.List;

public interface GiftCertificateService {
    List<GiftCertificateDto> findAll(String tagName, String certificateName, String orderingName,
                                     String certificateDescription, String orderingCreateDate);
    GiftCertificateDto findById(long id);

    GiftCertificateDto create(GiftCertificateDto certificate);
    GiftCertificateDto update(GiftCertificateDto certificate);
    void delete(long id);
}
