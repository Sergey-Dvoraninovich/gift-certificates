package com.epam.esm.repository;

import com.epam.esm.TestProfileResolver;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.repository.OrderingType.ASC;
import static com.epam.esm.repository.OrderingType.DESC;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TestDatabaseConfig.class)
@ActiveProfiles(resolver = TestProfileResolver.class)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {"classpath:init_data.sql"})
public class GiftCertificateRepositoryTestFind {
    private static final Integer PAGE_NUMBER = 1;
    private static final Integer PAGE_SIZE = 10;

    @Autowired
    private GiftCertificateRepository giftCertificateRepository;

    @Test
    void testCount() {
        List<GiftCertificate> expected = Arrays.asList(provideMultipleTagsGiftCertificate());

       long actual = giftCertificateRepository.countAll(Arrays.asList("first tag"), null, null,
                null, null);

        assertEquals(expected.size(), actual);
    }

    @Test
    void testFindByTagName() {
        List<GiftCertificate> expected = Arrays.asList(provideMultipleTagsGiftCertificate());

        List<GiftCertificate> actual = giftCertificateRepository.findAll(Arrays.asList("first tag"), null, null,
                                                          null, null, PAGE_NUMBER, PAGE_SIZE);

        assertEquals(expected, actual);
    }

    @Test
    void testFindSeveralByTagNames() {
        List<GiftCertificate> expected = Arrays.asList(provideMultipleTagsGiftCertificate());

        List<GiftCertificate> actual = giftCertificateRepository.findAll(Arrays.asList("second", "first"), null, null,
                null, null, PAGE_NUMBER, PAGE_SIZE);

        assertEquals(expected, actual);
    }

    @Test
    void testFindByName() {
        List<GiftCertificate> expected = Arrays.asList(provideMultipleTagsGiftCertificate());
        String name = expected.get(0).getName();

        List<GiftCertificate> actual = giftCertificateRepository.findAll(null, name, null,
                null, null, PAGE_NUMBER, PAGE_SIZE);

        assertEquals(expected, actual);
    }


    @Test
    void testFindByDescription() {
        List<GiftCertificate> expected = Arrays.asList(provideMultipleTagsGiftCertificate());
        String description = expected.get(0).getDescription();

        List<GiftCertificate> actual = giftCertificateRepository.findAll(null, null, null,
                description, null, PAGE_NUMBER, PAGE_SIZE);

        assertEquals(expected, actual);
    }

    @Test
    void testOrderByNameAsc() {
        long expectedFirstId = 3L;

        List<GiftCertificate> actual = giftCertificateRepository.findAll(null, null, ASC,
                null, null, PAGE_NUMBER, PAGE_SIZE);
        long actualFirstId = actual.get(0).getId();

        assertEquals(expectedFirstId, actualFirstId);
    }

    @Test
    void testOrderByCreationTimeDesc() {
        long expectedFirstId = 3L;

        List<GiftCertificate> actual = giftCertificateRepository.findAll(null, null, null,
                null, DESC, PAGE_NUMBER, PAGE_SIZE);
        long actualFirstId = actual.get(0).getId();

        assertEquals(expectedFirstId, actualFirstId);
    }

    @Test
    void findByAllParams(){
        GiftCertificate certificate = provideSingleTagCertificate();
        String name = certificate.getName();
        String description = certificate.getDescription();
        long expectedFirstId = certificate.getId();

        List<GiftCertificate> actual = giftCertificateRepository.findAll(Arrays.asList("second tag"), name, ASC, description, DESC, PAGE_NUMBER, PAGE_SIZE);
        long actualFirstId = actual.get(0).getId();

        assertEquals(expectedFirstId, actualFirstId);
    }

    @Test
    void testFindById() {
        GiftCertificate expected = provideMultipleTagsGiftCertificate();

        Optional<GiftCertificate> actual = giftCertificateRepository.findById(expected.getId());

        assertEquals(expected, actual.get());
    }

    //stored in DB
    private GiftCertificate provideMultipleTagsGiftCertificate() {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(1L);
        giftCertificate.setName("certificate first and second tags");
        giftCertificate.setDescription("certificate with first tag and second tags");
        giftCertificate.setPrice(new BigDecimal("50.00"));
        giftCertificate.setDuration(Duration.ofDays(90));
        Instant date = Instant.from(ZonedDateTime.of(2000, 1, 1, 11, 11, 11, 222000000, ZoneId.of("Europe/Minsk")));
        giftCertificate.setCreateDate(date);
        giftCertificate.setLastUpdateDate(date);
        giftCertificate.setGiftCertificateTags(provideTagsList());
        return giftCertificate;
    }

    //stored in DB
    private GiftCertificate provideSingleTagCertificate() {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(2L);
        giftCertificate.setName("certificate second tag");
        giftCertificate.setDescription("certificate with second tag");
        giftCertificate.setPrice(new BigDecimal("100.00"));
        giftCertificate.setDuration(Duration.ofDays(180));
        Instant date = Instant.from(ZonedDateTime.of(2011, 1, 1, 11, 11, 11, 222000000, ZoneId.of("Europe/Minsk")));
        giftCertificate.setCreateDate(date);
        giftCertificate.setLastUpdateDate(date);
        giftCertificate.setGiftCertificateTags(Arrays.asList(provideTagsList().get(1)));
        return giftCertificate;
    }

    //stored in DB
    private GiftCertificate provideCertificateWithoutTag() {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(3L);
        giftCertificate.setName("certificate");
        giftCertificate.setDescription("certificate");
        giftCertificate.setPrice(new BigDecimal("200.00"));
        giftCertificate.setDuration(Duration.ofDays(180));
        Instant date = Instant.from(ZonedDateTime.of(2022, 1, 1, 11, 11, 11, 222000000, ZoneId.of("Europe/Minsk")));
        giftCertificate.setCreateDate(date);
        giftCertificate.setLastUpdateDate(date);
        return giftCertificate;
    }

    private GiftCertificate provideGiftCertificate() {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(4L);
        giftCertificate.setName("test certificate");
        giftCertificate.setDescription("New certificate for test");
        giftCertificate.setPrice(new BigDecimal("200.00"));
        giftCertificate.setDuration(Duration.ofDays(365));
        return giftCertificate;
    }

    private List<Tag> provideTagsList() {
        Tag firstTag = new Tag();
        firstTag.setId(1L);
        firstTag.setName("first tag");

        Tag secondTag = new Tag();
        secondTag.setId(2L);
        secondTag.setName("second tag");

        return Arrays.asList(firstTag, secondTag);
    }
}
