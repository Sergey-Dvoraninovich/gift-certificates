package com.epam.esm.repository.Impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.OrderingType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.Instant;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class GiftCertificateRepositoryImpl implements GiftCertificateRepository {
    private static final String GIFT_CERTIFICATE_NAME_PARAM = "giftCertificateName";

    private static final String ALL_GIFT_CERTIFICATES
            = "SELECT c FROM GiftCertificate c";

    private static final String GIFT_CERTIFICATE_BY_NAME
            = "SELECT c FROM GiftCertificate c WHERE c.name = :giftCertificateName";

    @PersistenceContext
    private final EntityManager entityManager;

    //TODO implement findAll
    @Override
    public List<GiftCertificate> findAll(String tagName, String certificateName, OrderingType orderingName,
                                         String certificateDescription, OrderingType orderingCreateDate) {
        return entityManager.createQuery(ALL_GIFT_CERTIFICATES, GiftCertificate.class)
                .getResultList();
    }

    @Override
    public Optional<GiftCertificate> findById(long id) {
        GiftCertificate giftCertificate = entityManager.find(GiftCertificate.class, id);
        return Optional.ofNullable(giftCertificate);
    }

    @Override
    public Optional<GiftCertificate> findByName(String name) {
        List<GiftCertificate> certificates = entityManager.createQuery(GIFT_CERTIFICATE_BY_NAME, GiftCertificate.class)
                .setParameter(GIFT_CERTIFICATE_NAME_PARAM, name)
                .getResultList();

        return certificates.size() == 0
                ? Optional.empty()
                : Optional.of(certificates.get(0));
    }

    @Override
    public GiftCertificate create(GiftCertificate certificate) {
        certificate.setCreateDate(Instant.now());
        certificate.setLastUpdateDate(Instant.now());

        entityManager.persist(certificate);
        return certificate;
    }

    @Override
    public GiftCertificate update(GiftCertificate certificate) {
        certificate.setLastUpdateDate(Instant.now());
        entityManager.merge(certificate);
        return certificate;
    }

    @Override
    public boolean delete(GiftCertificate certificate) {
        entityManager.remove(certificate);
        return entityManager.find(Tag.class, certificate.getId()) == null;
    }

}
