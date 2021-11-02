package com.epam.esm.dto.mapping;

import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = MapperConfiguration.class)
public class GiftCertificateDtoMapperTest {

    @Autowired
    private GiftCertificateDtoMapper giftCertificateDtoMapper;

    @Test
    void testToDtoTagsList() {
        List<TagDto> expected = provideDtoCertificateTagsList();

        List<Tag> tags = provideCertificateTagsList();
        GiftCertificate giftCertificate = provideGiftCertificate();
        List<TagDto> actual = giftCertificateDtoMapper.toDto(giftCertificate, tags).getTagsDto();

        assertEquals(expected, actual);
    }

    private List<Tag> provideCertificateTagsList() {
        Tag firstTag = new Tag();
        firstTag.setId(1L);
        firstTag.setName("test_tag1");

        Tag secondTag = new Tag();
        secondTag.setId(2L);
        secondTag.setName("test_tag2");

        return Arrays.asList(firstTag, secondTag);
    }

    private List<TagDto> provideDtoCertificateTagsList() {
        TagDto firstTag = new TagDto();
        firstTag.setId(1L);
        firstTag.setName("test_tag1");

        TagDto secondTag = new TagDto();
        secondTag.setId(2L);
        secondTag.setName("test_tag2");

        return Arrays.asList(firstTag, secondTag);
    }

    private GiftCertificate provideGiftCertificate() {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(1L);
        giftCertificate.setName("certificate_tags_1_2");
        giftCertificate.setDescription("certificate with test_tag1 and test_tag2");
        giftCertificate.setPrice(new BigDecimal("50.00"));
        giftCertificate.setDuration(Duration.ofDays(90));
        giftCertificate.setCreateDate(LocalDateTime.now());
        giftCertificate.setLastUpdateDate(LocalDateTime.now());
        return giftCertificate;
    }
}
