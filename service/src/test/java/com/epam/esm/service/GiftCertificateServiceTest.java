package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.mapping.GiftCertificateDtoMapper;
import com.epam.esm.dto.mapping.TagDtoMapper;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.impl.GiftCertificateServiceImpl;
import com.epam.esm.validator.GiftCertificateSearchParamsValidator;
import com.epam.esm.validator.GiftCertificateValidator;
import com.epam.esm.validator.TagValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.*;
import java.util.Collections;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GiftCertificateServiceTest {
    @InjectMocks
    private GiftCertificateServiceImpl giftCertificateService;

    @Mock
    private GiftCertificateRepository giftCertificateRepository;

    @Mock
    private GiftCertificateValidator giftCertificateValidator;

    @Mock
    private GiftCertificateDtoMapper giftCertificateDtoMapper;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private TagValidator tagValidator;

    @Mock
    private TagDtoMapper tagDtoMapper;

    @Mock
    private GiftCertificateSearchParamsValidator searchParamsValidator;

    @BeforeAll
    static void setUp() {
        MockitoAnnotations.openMocks(GiftCertificateServiceTest.class);
    }

    @Test
    void testFindAll() {
        GiftCertificateDto certificateDto = provideGiftCertificateDto();
        GiftCertificate certificate = provideGiftCertificate();
        List<Tag> tags = provideTagsList();
        when(searchParamsValidator.validate(null, null, null, null, null)).thenReturn(Collections.emptyList());
        when(giftCertificateRepository.findAll(null, null, null, null, null))
                .thenReturn(Arrays.asList(certificate));
        when(tagRepository.findByCertificateId(certificate.getId())).thenReturn(tags);
        when(giftCertificateDtoMapper.toDto(certificate, tags)).thenReturn(certificateDto);
        List<GiftCertificateDto> expected = Arrays.asList(certificateDto);

        List<GiftCertificateDto> actual = giftCertificateService.findAll(null, null, null, null, null);

        assertEquals(expected, actual);
    }

    @Test
    void testFindById() {
        GiftCertificateDto certificateDto = provideGiftCertificateDto();
        GiftCertificate certificate = provideGiftCertificate();
        List<Tag> tags = provideTagsList();
        when(giftCertificateRepository.findById(certificateDto.getId())).thenReturn(Optional.of(certificate));
        when(tagRepository.findByCertificateId(certificate.getId())).thenReturn(tags);
        when(giftCertificateDtoMapper.toDto(certificate, tags)).thenReturn(certificateDto);

        GiftCertificateDto actual = giftCertificateService.findById(certificateDto.getId());

        assertEquals(certificateDto, actual);
    }

    @Test
    void testCreate() {
        GiftCertificateDto certificateDto = provideGiftCertificateDto();
        GiftCertificate certificate = provideGiftCertificate();
        List<TagDto> tagsDto = provideTagsDtoList();
        List<Tag> tags = provideTagsList();

        when(giftCertificateDtoMapper.toEntity(certificateDto)).thenReturn(certificate);
        when(giftCertificateDtoMapper.toDto(certificate, tags)).thenReturn(certificateDto);
        when(giftCertificateValidator.validateWithRequiredParams(any(GiftCertificateDto.class))).thenReturn(Collections.emptyList());
        when(tagValidator.validateParams(any(String.class))).thenReturn(new ArrayList<>());

        when(giftCertificateRepository.findById(certificate.getId())).thenReturn(Optional.of(certificate));
        when(giftCertificateRepository.findByName(certificate.getName())).thenReturn(Optional.empty());
        when(giftCertificateRepository.create(any(GiftCertificate.class))).thenReturn(certificate.getId());
        for (int i = 0; i < tags.size(); i++){
            when(tagRepository.findByName(tags.get(i).getName())).thenReturn(Optional.empty());
            when(tagRepository.create(tags.get(i))).thenReturn(tags.get(i).getId());
            when(tagDtoMapper.toEntity(tagsDto.get(i))).thenReturn(tags.get(i));
        }

        GiftCertificateDto actual = giftCertificateService.create(certificateDto);

        assertEquals(certificateDto, actual);
    }

    @Test
    void testUpdate() {
        GiftCertificate certificate = provideGiftCertificate();
        List<TagDto> tagsDto = provideTagsDtoList();
        List<Tag> tags = provideTagsList();

        GiftCertificateDto updatedCertificateDto = provideGiftCertificateDto();
        updatedCertificateDto.setDescription("New description");
        GiftCertificate updatedCertificate = provideGiftCertificate();
        updatedCertificate.setDescription("New description");

        when(giftCertificateDtoMapper.toEntity(updatedCertificateDto)).thenReturn(updatedCertificate);
        when(giftCertificateDtoMapper.toDto(any(GiftCertificate.class), eq(tags))).thenReturn(updatedCertificateDto);

        when(giftCertificateValidator.validateParams(any(String.class), any(String.class), any(String.class),
                any(String.class))).thenReturn(Collections.emptyList());
        when(tagValidator.validateParams(any(String.class))).thenReturn(new ArrayList<>());

        when(giftCertificateRepository.findById(updatedCertificate.getId())).thenReturn(Optional.of(certificate));
        when(giftCertificateRepository.update(any(GiftCertificate.class))).thenReturn(true);
        for (int i = 0; i < tags.size(); i++){
            when(tagRepository.findByName(tags.get(i).getName())).thenReturn(Optional.empty());
            when(tagRepository.create(tags.get(i))).thenReturn(tags.get(i).getId());
            when(tagDtoMapper.toEntity(tagsDto.get(i))).thenReturn(tags.get(i));
        }

        GiftCertificateDto actual = giftCertificateService.update(updatedCertificateDto);

        assertEquals(updatedCertificateDto, actual);
    }

    @Test
    void testDelete() {
        GiftCertificate certificate = provideGiftCertificate();
        when(giftCertificateRepository.findById(certificate.getId())).thenReturn(Optional.of(certificate));

        giftCertificateService.delete(certificate.getId());
    }

    private GiftCertificate provideGiftCertificate() {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(1L);
        giftCertificate.setName("certificate first and second tags");
        giftCertificate.setDescription("certificate with first tag and second tag");
        giftCertificate.setPrice(new BigDecimal("50.00"));
        giftCertificate.setDuration(Duration.ofDays(90));
        Instant date = Instant.from(ZonedDateTime.of(2000, 1, 1, 11, 11, 11, 222000000, ZoneId.of("Europe/Minsk")));
        giftCertificate.setCreateDate(date);
        giftCertificate.setLastUpdateDate(date);
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

    private GiftCertificateDto provideGiftCertificateDto() {
        GiftCertificateDto certificate = new GiftCertificateDto();
        certificate.setId(1L);
        certificate.setName("certificate first and second tags");
        certificate.setDescription("certificate with first tag and second tag");
        certificate.setPrice(new BigDecimal("50.00"));
        certificate.setDuration(Duration.ofDays(90));
        certificate.setTagsDto(provideTagsDtoList());
        Instant date = Instant.from(ZonedDateTime.of(2000, 1, 1, 11, 11, 11, 222000000, ZoneId.of("Europe/Minsk")));
        certificate.setCreateDate(date);
        certificate.setLastUpdateDate(date);
        return certificate;
    }

    private List<TagDto> provideTagsDtoList() {
        TagDto firstTag = new TagDto();
        firstTag.setId(1L);
        firstTag.setName("first tag");

        TagDto secondTag = new TagDto();
        secondTag.setId(2L);
        secondTag.setName("second tag");

        return Arrays.asList(firstTag, secondTag);
    }
}
