package com.epam.esm.service.impl;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.mapping.GiftCertificateDtoMapper;
import com.epam.esm.dto.mapping.TagDtoMapper;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.InvalidEntityException;
import com.epam.esm.handler.PaginationHandler;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.OrderingType;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.validator.GiftCertificateSearchParamsValidator;
import com.epam.esm.validator.GiftCertificateValidator;
import com.epam.esm.validator.TagValidator;
import com.epam.esm.validator.ValidationError;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
    private final GiftCertificateDtoMapper giftCertificateDtoMapper;
    private final TagDtoMapper tagDtoMapper;
    private final PaginationHandler paginationHandler;

    @Override
    public List<GiftCertificateDto> findAll(String[] tagNames, String certificateName, String orderingName,
                                            String certificateDescription, String orderingCreateDate,
                                            Integer pageNumber, Integer pageSize) {
        paginationHandler.handle(pageNumber, pageSize);
        int minPos = paginationHandler.getMinPos();
        int maxPos = paginationHandler.getMaxPos();

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
                                                                                   minPos, maxPos);
        List<GiftCertificateDto> certificatesDto = certificates.stream()
                .map(giftCertificateDtoMapper::toDto)
                .collect(Collectors.toList());
        return certificatesDto;
    }

    @Override
    public GiftCertificateDto findById(long id) {
        Optional<GiftCertificate> certificateOptional = giftCertificateRepository.findById(id);
        if (certificateOptional.isPresent()) {
            return giftCertificateDtoMapper.toDto(certificateOptional.get());
        }
        else {
            throw new EntityNotFoundException(id, GiftCertificateDto.class);
        }
    }

    @Override
    @Transactional
    public GiftCertificateDto create(GiftCertificateDto certificateDto) {
        List<ValidationError> validationErrors = giftCertificateValidator.validateWithRequiredParams(certificateDto);

        if (!validationErrors.isEmpty()) {
            throw new InvalidEntityException(validationErrors, GiftCertificateDto.class);
        }

        GiftCertificate certificate = giftCertificateDtoMapper.toEntity(certificateDto);
        String name = certificate.getName();
        if (giftCertificateRepository.findByName(name).isPresent()) {
            throw new EntityAlreadyExistsException(GiftCertificateDto.class);
        }

        List<Tag> certificateTags = processTags(certificateDto.getTagsDto());
        certificate.setGiftCertificateTags(certificateTags);
        certificate = giftCertificateRepository.create(certificate);
        return giftCertificateDtoMapper.toDto(certificate);
    }

    @Override
    @Transactional
    public GiftCertificateDto update(GiftCertificateDto certificateDto) {
        GiftCertificate certificate = giftCertificateDtoMapper.toEntity(certificateDto);

        int updatedFieldsAmount = countFieldsToUpdate(certificateDto);
        if (updatedFieldsAmount == 0) {
            throw new InvalidEntityException(Collections.singletonList(NO_GIFT_CERTIFICATE_FIELDS_TO_UPDATE), GiftCertificateDto.class);
        }
        if (updatedFieldsAmount > 1) {
            throw new InvalidEntityException(Collections.singletonList(IMPOSSIBLE_TO_UPDATE_SEVERAL_GIFT_CERTIFICATE_FIELDS), GiftCertificateDto.class);
        }

        String priceString = certificate.getPrice() == null ? null : certificate.getPrice().toString();
        String durationString = certificate.getDuration() == null ? null : String.valueOf(certificate.getDuration().toDays());
        List<ValidationError> validationErrors = giftCertificateValidator.validateParams(certificate.getName(), certificate.getDescription(),
                priceString, durationString);

        if (!validationErrors.isEmpty()) {
            throw new InvalidEntityException(validationErrors, GiftCertificateDto.class);
        }

        GiftCertificate storedCertificate;
        long id = certificate.getId();
        Optional<GiftCertificate> optionalCertificate = giftCertificateRepository.findById(id);
        if (!optionalCertificate.isPresent()) {
            throw new EntityNotFoundException(id, GiftCertificateDto.class);
        }
        else {
            storedCertificate = optionalCertificate.get();
        }

        if (certificateDto.getTagsDto() != null) {
            List<Tag> certificateTags = processTags(certificateDto.getTagsDto());
            certificate.setGiftCertificateTags(certificateTags);
        }

        updateFields(storedCertificate, certificate);
        System.out.println(storedCertificate);
        storedCertificate = giftCertificateRepository.update(storedCertificate);
        return giftCertificateDtoMapper.toDto(storedCertificate);
    }

    @Override
    @Transactional
    public void delete(long id) {
        Optional<GiftCertificate> giftCertificate = giftCertificateRepository.findById(id);
        if (!giftCertificate.isPresent()){
            throw new EntityNotFoundException(id, GiftCertificateDto.class);
        }
        giftCertificateRepository.delete(giftCertificate.get());
    }

    private void updateFields(GiftCertificate storedCertificate, GiftCertificate certificate){
        String name = certificate.getName();
        String description = certificate.getDescription();
        BigDecimal price = certificate.getPrice();
        Duration duration = certificate.getDuration();
        List<Tag> certificateTags = certificate.getGiftCertificateTags();

        storedCertificate.setName(name == null ? storedCertificate.getName() : name);
        storedCertificate.setDescription(description == null ? storedCertificate.getDescription() : description);
        storedCertificate.setPrice(price == null ? storedCertificate.getPrice() : price);
        storedCertificate.setDuration(duration == null ? storedCertificate.getDuration() : duration);
        storedCertificate.setGiftCertificateTags(certificateTags == null ? storedCertificate.getGiftCertificateTags() : certificateTags);
    }

    private int countFieldsToUpdate(GiftCertificateDto certificateDto) {
        int changedFieldsAmount = 0;
        changedFieldsAmount += certificateDto.getName() == null ? 0 : 1;
        changedFieldsAmount += certificateDto.getDescription() == null ? 0 : 1;
        changedFieldsAmount += certificateDto.getDuration() == null ? 0 : 1;
        changedFieldsAmount += certificateDto.getPrice() == null ? 0 : 1;
        changedFieldsAmount += certificateDto.getTagsDto() == null ? 0 : 1;
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
}
