package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class GiftCertificateRepositoryTestFind {
    private static final Integer PAGE_NUMBER = 0;
    private static final Integer PAGE_SIZE = 10;

    @Autowired
    private GiftCertificateRepository giftCertificateRepository;

    @Test
    void testCount() {
        //Given
        List<GiftCertificate> expected = List.of(provideMultipleTagsGiftCertificate());

        Specification<GiftCertificate> specification = new GiftCertificateSpecificationBuilder()
                .certificateTagNames(List.of(provideTagsList().get(0)))
                .build();

        //When
        long actual = giftCertificateRepository.count(specification);

        //Then
        assertEquals(expected.size(), actual);
    }

    @Test
    void testFindByTagName() {
        //Given
        List<GiftCertificate> expected = List.of(provideMultipleTagsGiftCertificate());

        Specification<GiftCertificate> specification = new GiftCertificateSpecificationBuilder()
                .certificateTagNames(List.of(provideTagsList().get(0)))
                .build();

        //When
        Page<GiftCertificate> page = giftCertificateRepository.findAll(specification,
                PageRequest.of(PAGE_NUMBER, PAGE_SIZE));

        //Then
        List<GiftCertificate> actual = page.stream().toList();
        assertEquals(expected, actual);
    }

    @Test
    void testFindSeveralByTagNames() {
        //Given
        List<GiftCertificate> expected = Arrays.asList(provideMultipleTagsGiftCertificate());

        Specification<GiftCertificate> specification = new GiftCertificateSpecificationBuilder()
                .certificateTagNames(provideTagsList())
                .build();

        //When
        Page<GiftCertificate> page = giftCertificateRepository.findAll(specification,
                PageRequest.of(PAGE_NUMBER, PAGE_SIZE));

        //Then
        List<GiftCertificate> actual = page.stream().toList();
        assertEquals(expected, actual);
    }

    @Test
    void testFindByName() {
        //Given
        List<GiftCertificate> expected = Arrays.asList(provideMultipleTagsGiftCertificate());
        String name = expected.get(0).getName();

        Specification<GiftCertificate> specification = new GiftCertificateSpecificationBuilder()
                .certificateName(name)
                .build();

        //When
        Page<GiftCertificate> page = giftCertificateRepository.findAll(specification,
                PageRequest.of(PAGE_NUMBER, PAGE_SIZE));

        //Then
        List<GiftCertificate> actual = page.stream().toList();
        assertEquals(expected, actual);
    }


    @Test
    void testFindByDescription() {
        //Given
        List<GiftCertificate> expected = Arrays.asList(provideMultipleTagsGiftCertificate());
        String description = expected.get(0).getDescription();

        Specification<GiftCertificate> specification = new GiftCertificateSpecificationBuilder()
                .certificateDescription(description)
                .build();

        //When
        Page<GiftCertificate> page = giftCertificateRepository.findAll(specification,
                PageRequest.of(PAGE_NUMBER, PAGE_SIZE));

        //Then
        List<GiftCertificate> actual = page.stream().toList();
        assertEquals(expected, actual);
    }

    @Test
    void findByAllParams(){
        //Given
        GiftCertificate certificate = provideSingleTagCertificate();
        String name = certificate.getName();
        String description = certificate.getDescription();
        long expectedFirstId = certificate.getId();

        Specification<GiftCertificate> specification = new GiftCertificateSpecificationBuilder()
                .certificateName(name)
                .certificateDescription(description)
                .certificateTagNames(List.of(provideTagsList().get(1)))
                .build();

        //When
        Page<GiftCertificate> actual = giftCertificateRepository.findAll(specification,
                PageRequest.of(PAGE_NUMBER, PAGE_SIZE));

        //Then
        long actualFirstId = actual.stream().toList().get(0).getId();
        assertEquals(expectedFirstId, actualFirstId);
    }

    @Test
    void testFindById() {
        //Given
        GiftCertificate expected = provideMultipleTagsGiftCertificate();

        //When
        Optional<GiftCertificate> actual = giftCertificateRepository.findById(expected.getId());

        //When
        assertEquals(expected, actual.get());
    }

    //stored in DB
    private GiftCertificate provideMultipleTagsGiftCertificate() {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(1L);
        giftCertificate.setIsAvailable(true);
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
        giftCertificate.setIsAvailable(true);
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
        giftCertificate.setIsAvailable(true);
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
        giftCertificate.setIsAvailable(true);
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
