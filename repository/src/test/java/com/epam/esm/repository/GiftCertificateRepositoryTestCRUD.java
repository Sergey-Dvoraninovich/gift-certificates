package com.epam.esm.repository;

import com.epam.esm.TestProfileResolver;
import com.epam.esm.entity.GiftCertificate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestDatabaseConfig.class)
@ActiveProfiles(resolver = TestProfileResolver.class)
@Transactional
public class GiftCertificateRepositoryTestCRUD {
    @Autowired
    private GiftCertificateRepository giftCertificateRepository;

    @Test
    void testCreate() {
        //Given
        GiftCertificate giftCertificate = provideNewGiftCertificate();
        Instant date = Instant.now();
        giftCertificate.setCreateDate(date);
        giftCertificate.setLastUpdateDate(date);

        //When
        GiftCertificate certificate = giftCertificateRepository.create(giftCertificate);

        //Then
        assertNotNull(certificate);
        assertTrue(certificate.getId() > 0);
    }

    @Test
    void testUpdate() {
        //Given
        GiftCertificate certificate = provideNewGiftCertificate();
        Instant date = Instant.now();
        certificate.setCreateDate(date);
        certificate.setLastUpdateDate(date);
        BigDecimal newPrice = new BigDecimal("109.99");

        //Preparation
        GiftCertificate storedCertificate = provideStoredGiftCertificate(certificate);
        storedCertificate.setPrice(newPrice);

        //When
        certificate = giftCertificateRepository.create(storedCertificate);

        //Then
        assertNotNull(certificate);
        assertEquals(storedCertificate, certificate);

        //Clean
        removeRedundantGiftCertificate(certificate);
    }

    @Test
    void testDelete() {
        //Given
        GiftCertificate storedCertificate = provideNewGiftCertificate();
        Instant date = Instant.now();
        storedCertificate.setCreateDate(date);
        storedCertificate.setLastUpdateDate(date);

        //Preparation
        storedCertificate = giftCertificateRepository.create(storedCertificate);
        assertNotNull(storedCertificate);
        assertTrue(storedCertificate.getId() > 0);

        //When
        boolean actual = giftCertificateRepository.delete(storedCertificate);

        //Then
        assertTrue(actual);
        Optional<GiftCertificate> deletedCertificate = giftCertificateRepository.findById(storedCertificate.getId());
        assertFalse(deletedCertificate.isPresent());
    }

    private GiftCertificate provideNewGiftCertificate() {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setName("test certificate");
        giftCertificate.setDescription("New certificate for test");
        giftCertificate.setPrice(new BigDecimal("200.00"));
        giftCertificate.setDuration(Duration.ofDays(365));
        return giftCertificate;
    }

    private GiftCertificate provideStoredGiftCertificate(GiftCertificate certificate) {

        long generatedId = giftCertificateRepository.create(certificate).getId();
        assertTrue(generatedId > 0);

        return certificate;
    }

    private void removeRedundantGiftCertificate(GiftCertificate certificate) {
        giftCertificateRepository.delete(certificate);
    }
}
