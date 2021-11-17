package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;

import java.util.List;
import java.util.Optional;

public interface GiftCertificateRepository {

    List<GiftCertificate> findAll(List<String> tagNames, String certificateName, OrderingType orderingName,
                                  String certificateDescription, OrderingType orderingCreateDate,
                                  int pageNumber, int pageSize);
    Optional<GiftCertificate> findById(long id);
    Optional<GiftCertificate> findByName(String name);


    GiftCertificate create(GiftCertificate certificate);
    GiftCertificate update(GiftCertificate certificate);
    boolean delete(GiftCertificate certificate);
}
