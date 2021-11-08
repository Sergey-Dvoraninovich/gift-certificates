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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    public List<GiftCertificateDto> findAll(String tagName, String certificateName, String orderingName,
                                            String certificateDescription, String orderingCreateDate) {
        List<ValidationError> validationErrors = searchParamsValidator.validate(tagName, certificateName, orderingName,
                                                                                certificateDescription, orderingCreateDate);
        if (!validationErrors.isEmpty()) {
            throw new InvalidEntityException(validationErrors, String.class);
        }

        OrderingType orderingNameType = orderingName == null ? null : OrderingType.valueOf(orderingName);
        OrderingType orderingCreateDateType = orderingCreateDate == null ? null : OrderingType.valueOf(orderingCreateDate);
        List<GiftCertificate> giftCertificates = giftCertificateRepository.findAll(tagName, certificateName, orderingNameType,
                                                                                   certificateDescription, orderingCreateDateType);
        List<GiftCertificateDto> giftCertificatesDto = new ArrayList<>();
        for (GiftCertificate certificate: giftCertificates) {
            List<Tag> tags = tagRepository.findByCertificateId(certificate.getId());
            giftCertificatesDto.add(giftCertificateDtoMapper.toDto(certificate, tags));
        }
        return giftCertificatesDto;
    }

    @Override
    public GiftCertificateDto findById(long id) {
        Optional<GiftCertificate> certificateOptional = giftCertificateRepository.findById(id);
        if (certificateOptional.isPresent()) {
            List<Tag> certificateTags = tagRepository.findByCertificateId(id);
            return giftCertificateDtoMapper.toDto(certificateOptional.get(), certificateTags);
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

        long certificateId = giftCertificateRepository.create(certificate);
        certificate.setId(certificateId);

        List<Tag> certificateTags;
        if (certificateDto.getTagsDto() != null) {
            certificateTags = processTags(certificateDto.getTagsDto());
            updateCertificateTagsRepository(certificate, certificateTags);
        }
        else {
            certificateTags = new ArrayList<>();
        }

        certificate = giftCertificateRepository.findById(certificateId).get();
        return giftCertificateDtoMapper.toDto(certificate, certificateTags);
    }

    @Override
    @Transactional
    public GiftCertificateDto update(GiftCertificateDto certificateDto) {
        GiftCertificate certificate = giftCertificateDtoMapper.toEntity(certificateDto);

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

        updateFields(storedCertificate, certificate);
        giftCertificateRepository.update(storedCertificate);

        List<Tag> certificateTags;
        if (certificateDto.getTagsDto() != null) {
            certificateTags = processTags(certificateDto.getTagsDto());
            updateCertificateTagsRepository(storedCertificate, certificateTags);
        }
        else {
            certificateTags = tagRepository.findByCertificateId(storedCertificate.getId());
        }

        storedCertificate = giftCertificateRepository.findById(id).get();
        return giftCertificateDtoMapper.toDto(storedCertificate, certificateTags);
    }

    @Override
    @Transactional
    public void delete(long id) {
        Optional<GiftCertificate> giftCertificate = giftCertificateRepository.findById(id);
        if (!giftCertificate.isPresent()){
            throw new EntityNotFoundException(id, GiftCertificateDto.class);
        }
        giftCertificateRepository.delete(id);
    }

    private void updateFields(GiftCertificate storedCertificate, GiftCertificate certificate){
        String name = certificate.getName();
        String description = certificate.getDescription();
        BigDecimal price = certificate.getPrice();
        Duration duration = certificate.getDuration();

        storedCertificate.setName(name == null ? storedCertificate.getName() : name);
        storedCertificate.setDescription(description == null ? storedCertificate.getDescription() : description);
        storedCertificate.setPrice(price == null ? storedCertificate.getPrice() : price);
        storedCertificate.setDuration(duration == null ? storedCertificate.getDuration() : duration);
    }

    private List<Tag> processTags(List<TagDto> certificateTagsDto){
        List<Tag> certificateTags = certificateTagsDto
                    .stream()
                    .map(tagDtoMapper::toEntity)
                    .collect(Collectors.toList());
        for (Tag tag: certificateTags) {
            Optional<Tag> tagOptional = tagRepository.findByName(tag.getName());

            if (!tagOptional.isPresent()) {
                List<ValidationError> validationErrors = tagValidator.validateParams(tag.getName());

                if (!validationErrors.isEmpty()) {
                    throw new InvalidEntityException(validationErrors, TagDto.class);
                }

                long tagId;
                tagId = tagRepository.create(tag);
                tag.setId(tagId);
            } else {
                tag = tagOptional.get();
            }
        }
        return certificateTags;
    }

    private void updateCertificateTagsRepository(GiftCertificate certificate, List<Tag> certificateTags) {
        List<Tag> storedCertificateTags = tagRepository.findByCertificateId(certificate.getId());

        List<Tag> tagsToRemove = storedCertificateTags.stream()
                .filter(tag -> !certificateTags.contains(tag))
                .collect(Collectors.toList());
        for (Tag tag: tagsToRemove) {
            giftCertificateRepository.removeCertificateTag(certificate.getId(), tag.getId());
        }

        List<Tag> tagsToAdd = certificateTags.stream()
                .filter(tag -> !storedCertificateTags.contains(tag))
                .collect(Collectors.toList());
        for (Tag tag: tagsToAdd) {
            giftCertificateRepository.addCertificateTag(certificate.getId(), tag.getId());
        }
    }
}
