package com.epam.esm.service;

import com.epam.esm.dto.*;
import com.epam.esm.dto.mapping.GiftCertificateRequestDtoMapper;
import com.epam.esm.dto.mapping.GiftCertificateResponseDtoMapper;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.InvalidEntityException;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.impl.GiftCertificateServiceImpl;
import com.epam.esm.validator.GiftCertificateRequestValidator;
import com.epam.esm.validator.GiftCertificateSearchParamsValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GiftCertificateServiceTest {
    private static final Integer PAGE_NUMBER = 1;
    private static final Integer PAGE_SIZE = 10;

    @InjectMocks
    private GiftCertificateServiceImpl giftCertificateService;

    @Mock
    private GiftCertificateRepository giftCertificateRepository;
    @Mock
    private TagRepository tagRepository;

    @Mock
    private GiftCertificateRequestDtoMapper giftCertificateRequestDtoMapper;
    @Mock
    private GiftCertificateResponseDtoMapper giftCertificateResponseDtoMapper;

    @Mock
    private GiftCertificateRequestValidator giftCertificateRequestValidator;
    @Mock
    private GiftCertificateSearchParamsValidator searchParamsValidator;

    @BeforeAll
    static void setUp() {
        MockitoAnnotations.openMocks(GiftCertificateServiceTest.class);
    }

    @Test
    void testFindAll() {
        GiftCertificateResponseDto certificateDto = provideGiftCertificateResponseDto();
        GiftCertificate certificate = provideGiftCertificate();
        Page<GiftCertificate> certificatesPage = new PageImpl<>(List.of(certificate));
        when(giftCertificateRepository.findAll(any(Specification.class), any(PageRequest.class)))
                .thenReturn(certificatesPage);
        when(giftCertificateResponseDtoMapper.toDto(certificate)).thenReturn(certificateDto);
        List<GiftCertificateResponseDto> expected = List.of(certificateDto);

        GiftCertificateFilterDto giftCertificateFilterDto = new GiftCertificateFilterDto();
        giftCertificateFilterDto.setShowDisabled(false);

        List<GiftCertificateResponseDto> actual = giftCertificateService.findAll(giftCertificateFilterDto, new PageDto(PAGE_NUMBER, PAGE_SIZE));

        assertEquals(expected, actual);
    }

    @Test
    void testFindById() {
        GiftCertificateResponseDto certificateDto = provideGiftCertificateResponseDto();
        GiftCertificate certificate = provideGiftCertificate();
        when(giftCertificateRepository.findById(certificateDto.getId())).thenReturn(Optional.of(certificate));
        when(giftCertificateResponseDtoMapper.toDto(certificate)).thenReturn(certificateDto);

        GiftCertificateResponseDto actual = giftCertificateService.findById(certificateDto.getId());

        assertEquals(certificateDto, actual);
    }

    @Test
    void testCreate() {
        GiftCertificateRequestDto certificateDto = provideGiftCertificateRequestDto();
        GiftCertificateResponseDto certificateResponseDto = provideGiftCertificateResponseDto();
        GiftCertificate certificate = provideGiftCertificate();
        List<Tag> tags = provideTagsList();

        when(giftCertificateRequestValidator.validateWithRequiredParams(certificateDto)).thenReturn(Collections.emptyList());
        when(giftCertificateRepository.findByName(certificate.getName())).thenReturn(Optional.empty());
        when(giftCertificateRequestDtoMapper.toEntity(certificateDto)).thenReturn(certificate);
        when(giftCertificateResponseDtoMapper.toDto(certificate)).thenReturn(certificateResponseDto);
        when(tagRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        when(giftCertificateRepository.save(any(GiftCertificate.class))).thenReturn(certificate);
        for (int i = 0; i < tags.size(); i++){
            when(tagRepository.findById(tags.get(i).getId())).thenReturn(Optional.of(tags.get(i)));
        }

        GiftCertificateResponseDto actual = giftCertificateService.create(certificateDto);

        assertEquals(certificateResponseDto, actual);
    }

    @Test
    void testUpdateTags() {
        GiftCertificate certificate = provideGiftCertificate();
        List<Tag> tags = provideTagsListForUpdate();

        GiftCertificate updatedCertificate = provideGiftCertificate();
        updatedCertificate.setGiftCertificateTags(tags);

        GiftCertificateRequestDto certificateRequestDto = GiftCertificateRequestDto.builder().build();
        certificateRequestDto.setTagIdsDto(provideTagIdsForUpdateList());
        GiftCertificateResponseDto certificateResponseDto = provideGiftCertificateResponseDto();
        certificateResponseDto.setTagsDto(provideTagsDtoForUpdateList());

        when(giftCertificateRepository.findById(certificate.getId())).thenReturn(Optional.of(certificate));
        when(giftCertificateResponseDtoMapper.toDto(certificate)).thenReturn(certificateResponseDto);

        for (int i = 0; i < tags.size(); i++){
            when(tagRepository.findById(tags.get(i).getId())).thenReturn(Optional.of(tags.get(i)));
        }

        when(giftCertificateRepository.save(any(GiftCertificate.class))).thenReturn(certificate);

        GiftCertificateResponseDto actual = giftCertificateService.update(certificate.getId(), certificateRequestDto);

        assertEquals(certificateResponseDto, actual);
    }

    @Test
    void testUpdateSingleField() {
        GiftCertificate certificate = provideGiftCertificate();

        GiftCertificate updatedCertificate = provideGiftCertificate();
        updatedCertificate.setDescription("New description");
        GiftCertificateRequestDto certificateRequestDto = GiftCertificateRequestDto.builder().build();
        certificateRequestDto.setDescription("New description");
        GiftCertificateResponseDto certificateResponseDto = provideGiftCertificateResponseDto();
        certificateResponseDto.setDescription("New description");

        when(giftCertificateRepository.findById(certificate.getId())).thenReturn(Optional.of(certificate));
        when(giftCertificateResponseDtoMapper.toDto(certificate)).thenReturn(certificateResponseDto);

        when(giftCertificateRepository.save(any(GiftCertificate.class))).thenReturn(certificate);

        GiftCertificateResponseDto actual = giftCertificateService.update(certificate.getId(), certificateRequestDto);

        assertEquals(certificateResponseDto, actual);
    }


    @Test
    void testUpdateNoFields() {
        long certificateId = 1L;
        GiftCertificateRequestDto certificateRequestDto = GiftCertificateRequestDto.builder().build();
        GiftCertificate certificate = provideGiftCertificate();
        when(giftCertificateRepository.findById(certificateId)).thenReturn(Optional.of(certificate));

        try {
            giftCertificateService.update(1L, certificateRequestDto);
            assertTrue(false);
        } catch (InvalidEntityException e) {
            assertTrue(true);
        }
    }

    @Test
    void testUpdateSeveralFields() {
        long certificateId = 1L;
        GiftCertificateRequestDto certificateRequestDto = GiftCertificateRequestDto.builder()
                .name("name")
                .description("description")
                .build();
        GiftCertificate certificate = provideGiftCertificate();
        when(giftCertificateRepository.findById(certificateId)).thenReturn(Optional.of(certificate));

        try {
            giftCertificateService.update(certificateId, certificateRequestDto);
            assertTrue(false);
        } catch (InvalidEntityException e) {
            assertTrue(true);
        }
    }

    @Test
    void testDisable() {
        GiftCertificate certificate = provideGiftCertificate();
        when(giftCertificateRepository.findById(certificate.getId())).thenReturn(Optional.of(certificate));

        giftCertificateService.disable(certificate.getId());

        assertFalse(certificate.getIsAvailable());
    }

    @Test
    void testMakeAvailable() {
        GiftCertificate certificate = provideGiftCertificate();
        certificate.setIsAvailable(false);
        when(giftCertificateRepository.findById(certificate.getId())).thenReturn(Optional.of(certificate));

        giftCertificateService.makeAvailable(certificate.getId());

        assertTrue(certificate.getIsAvailable());
    }

    private GiftCertificate provideGiftCertificate() {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(1L);
        giftCertificate.setIsAvailable(true);
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

    private List<Tag> provideTagsListForUpdate() {
        Tag firstTag = new Tag();
        firstTag.setId(1L);
        firstTag.setName("first tag");

        Tag secondTag = new Tag();
        secondTag.setId(2L);
        secondTag.setName("second tag");

        Tag thirdTag = new Tag();
        thirdTag.setId(3L);
        thirdTag.setName("third tag");

        return Arrays.asList(firstTag, secondTag, thirdTag);
    }

    private GiftCertificateResponseDto provideGiftCertificateResponseDto() {
        GiftCertificateResponseDto certificate = GiftCertificateResponseDto.builder()
                .id(1L)
                .isAvailable(true)
                .name("certificate first and second tags")
                .description("certificate with first tag and second tag")
                .price(new BigDecimal("50.00"))
                .duration(Duration.ofDays(90))
                .tagsDto(provideTagsDtoList())
                .build();

        Instant date = Instant.from(ZonedDateTime.of(2000, 1, 1, 11, 11, 11, 222000000, ZoneId.of("Europe/Minsk")));
        certificate.setCreateDate(date);
        certificate.setLastUpdateDate(date);
        return certificate;
    }

    private GiftCertificateRequestDto provideGiftCertificateRequestDto() {
        GiftCertificateRequestDto certificate = GiftCertificateRequestDto.builder()
                .name("certificate first and second tags")
                .description("certificate with first tag and second tag")
                .price(new BigDecimal("50.00"))
                .duration(Duration.ofDays(90))
                .tagIdsDto(provideTagIdsList())
                .build();
        return certificate;
    }

    private List<TagDto> provideTagsDtoList() {
        TagDto firstTag = TagDto.builder()
                .id(1L)
                .name("first tag")
                .build();

        TagDto secondTag = TagDto.builder()
                .id(2L)
                .name("second tag")
                .build();

        return Arrays.asList(firstTag, secondTag);
    }

    private List<TagDto> provideTagsDtoForUpdateList() {
        TagDto firstTag = TagDto.builder()
                .id(1L)
                .name("first tag")
                .build();

        TagDto secondTag = TagDto.builder()
                .id(2L)
                .name("second tag")
                .build();

        TagDto thirdTag = TagDto.builder()
                .id(3L)
                .name("third tag")
                .build();

        return Arrays.asList(firstTag, secondTag, thirdTag);
    }

    private List<Long> provideTagIdsList() {
        return Arrays.asList(new Long[]{1L, 2L});
    }

    private List<Long> provideTagIdsForUpdateList() {
        return Arrays.asList(new Long[]{1L, 2L, 3L});
    }
}
