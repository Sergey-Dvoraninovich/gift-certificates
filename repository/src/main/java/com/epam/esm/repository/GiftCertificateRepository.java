package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;

import java.util.List;
import java.util.Optional;

public interface GiftCertificateRepository {

    List<GiftCertificate> findAll(String tagName, String certificateName, OrderingType orderingName,
                                  String certificateDescription, OrderingType orderingCreateDate);
    Optional<GiftCertificate> findById(long id);
    Optional<GiftCertificate> findByName(String name);


    GiftCertificate create(GiftCertificate certificate);
    GiftCertificate update(GiftCertificate certificate);
    boolean delete(GiftCertificate certificate);
}
