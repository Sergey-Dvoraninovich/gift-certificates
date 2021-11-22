package com.epam.esm.service.impl;

import com.epam.esm.dto.*;
import com.epam.esm.dto.mapping.GiftCertificateRequestDtoMapper;
import com.epam.esm.dto.mapping.GiftCertificateResponseDtoMapper;
import com.epam.esm.dto.mapping.TagDtoMapper;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.InvalidEntityException;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.OrderingType;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.validator.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

import static com.epam.esm.validator.ValidationError.IMPOSSIBLE_TO_UPDATE_SEVERAL_GIFT_CERTIFICATE_FIELDS;
import static com.epam.esm.validator.ValidationError.NO_GIFT_CERTIFICATE_FIELDS_TO_UPDATE;

@Service
@RequiredArgsConstructor
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private final GiftCertificateRepository giftCertificateRepository;
    private final TagRepository tagRepository;
    private final GiftCertificateValidator giftCertificateValidator;
    private final TagValidator tagValidator;
    private final GiftCertificateSearchParamsValidator searchParamsValidator;
    private final GiftCertificateRequestDtoMapper giftCertificateRequestDtoMapper;
    private final GiftCertificateResponseDtoMapper giftCertificateResponseDtoMapper;
    private final TagDtoMapper tagDtoMapper;

    @Override
    public Long countAll(String[] tagNames, String certificateName, String orderingName, String certificateDescription, String orderingCreateDate) {
        List<String> tagNamesArray = tagNames == null ? null : Arrays.asList(tagNames);
        List<ValidationError> validationErrors = searchParamsValidator.validate(tagNamesArray, certificateName, orderingName,
                certificateDescription, orderingCreateDate);
        if (!validationErrors.isEmpty()) {
            throw new InvalidEntityException(validationErrors, String.class);
        }

        OrderingType orderingNameType = orderingName == null ? null : OrderingType.valueOf(orderingName);
        OrderingType orderingCreateDateType = orderingCreateDate == null ? null : OrderingType.valueOf(orderingCreateDate);

        return giftCertificateRepository.countAll(tagNamesArray, certificateName, orderingNameType,
                certificateDescription, orderingCreateDateType);
    }

    @Override
    public List<GiftCertificateResponseDto> findAll(String[] tagNames, String certificateName, String orderingName,
                                                    String certificateDescription, String orderingCreateDate,
                                                    Integer pageNumber, Integer pageSize) {

        List<String> tagNamesArray = tagNames == null ? null : Arrays.asList(tagNames);
        List<ValidationError> validationErrors = searchParamsValidator.validate(tagNamesArray, certificateName, orderingName,
                                                                                certificateDescription, orderingCreateDate);
        if (!validationErrors.isEmpty()) {
            throw new InvalidEntityException(validationErrors, String.class);
        }

        OrderingType orderingNameType = orderingName == null ? null : OrderingType.valueOf(orderingName);
        OrderingType orderingCreateDateType = orderingCreateDate == null ? null : OrderingType.valueOf(orderingCreateDate);
        List<GiftCertificate> certificates = giftCertificateRepository.findAll(tagNamesArray, certificateName, orderingNameType,
                                                                                   certificateDescription, orderingCreateDateType,
                                                                                   pageNumber, pageSize);
        return certificates.stream()
                .map(giftCertificateResponseDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public GiftCertificateResponseDto findById(long id) {
        Optional<GiftCertificate> certificateOptional = giftCertificateRepository.findById(id);
        if (certificateOptional.isPresent()) {
            return giftCertificateResponseDtoMapper.toDto(certificateOptional.get());
        }
        else {
            throw new EntityNotFoundException(id, GiftCertificateRequestDto.class);
        }
    }

    @Override
    @Transactional
    public GiftCertificateResponseDto create(GiftCertificateRequestDto certificateDto) {
        List<ValidationError> validationErrors = giftCertificateValidator.validateWithRequiredParams(certificateDto);

        if (!validationErrors.isEmpty()) {
            throw new InvalidEntityException(validationErrors, GiftCertificateRequestDto.class);
        }

        String name = certificateDto.getName();
        if (giftCertificateRepository.findByName(name).isPresent()) {
            throw new EntityAlreadyExistsException(GiftCertificateRequestDto.class);
        }

        List<Tag> certificateTags = new ArrayList<>();
        if(certificateDto.getTagIdsDto() != null) {
            certificateTags = processTagIds(certificateDto.getTagIdsDto());
        }

        GiftCertificate certificate = giftCertificateRequestDtoMapper.toEntity(certificateDto);
        certificate.setGiftCertificateTags(certificateTags);

        certificate = giftCertificateRepository.create(certificate);
        return giftCertificateResponseDtoMapper.toDto(certificate);
    }

    @Override
    @Transactional
    public GiftCertificateResponseDto update(long id, GiftCertificateRequestDto certificateDto) {

        int updatedFieldsAmount = countFieldsToUpdate(certificateDto);
        if (updatedFieldsAmount == 0) {
            throw new InvalidEntityException(Collections.singletonList(NO_GIFT_CERTIFICATE_FIELDS_TO_UPDATE), GiftCertificateRequestDto.class);
        }
        if (updatedFieldsAmount > 1) {
            throw new InvalidEntityException(Collections.singletonList(IMPOSSIBLE_TO_UPDATE_SEVERAL_GIFT_CERTIFICATE_FIELDS), GiftCertificateRequestDto.class);
        }

        String priceString = certificateDto.getPrice() == null ? null : certificateDto.getPrice().toString();
        String durationString = certificateDto.getDuration() == null ? null : String.valueOf(certificateDto.getDuration().toDays());
        List<ValidationError> validationErrors = giftCertificateValidator.validateParams(certificateDto.getName(), certificateDto.getDescription(),
                priceString, durationString);

        if (!validationErrors.isEmpty()) {
            throw new InvalidEntityException(validationErrors, GiftCertificateRequestDto.class);
        }

        GiftCertificate storedCertificate;
        Optional<GiftCertificate> optionalCertificate = giftCertificateRepository.findById(id);
        if (!optionalCertificate.isPresent()) {
            throw new EntityNotFoundException(id, GiftCertificateRequestDto.class);
        }
        else {
            storedCertificate = optionalCertificate.get();
        }

        if (certificateDto.getTagIdsDto() != null) {
            List<Tag> certificateTags = processTagIds(certificateDto.getTagIdsDto());
            storedCertificate.setGiftCertificateTags(certificateTags);
        }

        updateFieldsWithoutTags(storedCertificate, certificateDto);
        storedCertificate = giftCertificateRepository.update(storedCertificate);
        return giftCertificateResponseDtoMapper.toDto(storedCertificate);
    }

    @Override
    @Transactional
    public void delete(long id) {
        Optional<GiftCertificate> giftCertificate = giftCertificateRepository.findById(id);
        if (!giftCertificate.isPresent()){
            throw new EntityNotFoundException(id, GiftCertificateRequestDto.class);
        }
        giftCertificateRepository.delete(giftCertificate.get());
    }

    private void updateFieldsWithoutTags(GiftCertificate storedCertificate, GiftCertificateRequestDto certificate){
        String name = certificate.getName();
        String description = certificate.getDescription();
        BigDecimal price = certificate.getPrice();
        Duration duration = certificate.getDuration();

        storedCertificate.setName(name == null ? storedCertificate.getName() : name);
        storedCertificate.setDescription(description == null ? storedCertificate.getDescription() : description);
        storedCertificate.setPrice(price == null ? storedCertificate.getPrice() : price);
        storedCertificate.setDuration(duration == null ? storedCertificate.getDuration() : duration);
    }

    private int countFieldsToUpdate(GiftCertificateRequestDto certificateDto) {
        int changedFieldsAmount = 0;
        changedFieldsAmount += certificateDto.getName() == null ? 0 : 1;
        changedFieldsAmount += certificateDto.getDescription() == null ? 0 : 1;
        changedFieldsAmount += certificateDto.getDuration() == null ? 0 : 1;
        changedFieldsAmount += certificateDto.getPrice() == null ? 0 : 1;
        changedFieldsAmount += certificateDto.getTagIdsDto() == null ? 0 : 1;
        return changedFieldsAmount;
    }

    private List<Tag> processTags(List<TagDto> certificateTagsDto){
        List<Tag> certificateTags = certificateTagsDto
                .stream()
                .map(tagDtoMapper::toEntity)
                .collect(Collectors.toList());
        List<Tag> resultCertificateTags = new ArrayList<>();
        for (Tag tag: certificateTags) {
            Optional<Tag> tagOptional = tagRepository.findByName(tag.getName());

            if (!tagOptional.isPresent()) {
                List<ValidationError> validationErrors = tagValidator.validateParams(tag.getName());

                if (!validationErrors.isEmpty()) {
                    throw new InvalidEntityException(validationErrors, TagDto.class);
                }

                tag = tagRepository.create(tag);
            } else {
                tag = tagOptional.get();
            }
            resultCertificateTags.add(tag);
        }
        return resultCertificateTags;
    }

    private List<Tag> processTagIds(List<Long> certificateTagIds){
        List<Tag> resultCertificateTags = new ArrayList<>();
        for (Long tagId: certificateTagIds) {
            Optional<Tag> tagOptional = tagRepository.findById(tagId);

            if (!tagOptional.isPresent()) {
                resultCertificateTags.add(tagOptional.get());
            } else {
                throw new EntityNotFoundException(tagId, TagDto.class);
            }
        }
        return resultCertificateTags;
    }
}
