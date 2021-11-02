package com.epam.esm.service.impl;

import com.epam.esm.dto.GiftCertificateDto;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.time.ZoneOffset.UTC;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private GiftCertificateRepository giftCertificateRepository;
    private TagRepository tagRepository;
    private GiftCertificateValidator giftCertificateValidator;
    private TagValidator tagValidator;
    private GiftCertificateSearchParamsValidator searchParamsValidator;
    private GiftCertificateDtoMapper giftCertificateDtoMapper;
    private TagDtoMapper tagDtoMapper;

    public GiftCertificateServiceImpl(GiftCertificateRepository giftCertificateRepository,
                                      TagRepository tagRepository,
                                      GiftCertificateValidator giftCertificateValidator,
                                      TagValidator tagValidator,
                                      GiftCertificateSearchParamsValidator searchParamsValidator,
                                      GiftCertificateDtoMapper giftCertificateDtoMapper,
                                      TagDtoMapper tagDtoMapper) {
        this.giftCertificateRepository = giftCertificateRepository;
        this.tagRepository = tagRepository;
        this.giftCertificateValidator = giftCertificateValidator;
        this.tagValidator = tagValidator;
        this.searchParamsValidator = searchParamsValidator;
        this.giftCertificateDtoMapper = giftCertificateDtoMapper;
        this.tagDtoMapper = tagDtoMapper;
    }

    @Override
    public List<GiftCertificateDto> findAll(String tagName, String certificateName, String orderingName,
                                            String certificateDescription, String orderingCreateDate) {
        List<ValidationError> validationErrors = searchParamsValidator.validate(tagName, certificateName, orderingName,
                                                                                certificateDescription, orderingCreateDate);
        if (!validationErrors.isEmpty()) {
            throw new InvalidEntityException(validationErrors, GiftCertificate.class);
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
            throw new EntityNotFoundException(id, GiftCertificate.class);
        }
    }

    @Override
    @Transactional
    public GiftCertificateDto create(GiftCertificateDto certificateDto) {
        //TODO certificate null fix
        GiftCertificate certificate = giftCertificateDtoMapper.toEntity(certificateDto);

        List<ValidationError> validationErrors = giftCertificateValidator.validate(certificate.getName(), certificate.getDescription(),
                certificate.getPrice().toString(), String.valueOf(certificate.getDuration().toDays()),
                certificate.getCreateDate().toString(), certificate.getLastUpdateDate().toString());

        if (!validationErrors.isEmpty()) {
            throw new InvalidEntityException(validationErrors, GiftCertificate.class);
        }

        List<Tag> certificateTags = processTags(certificateDto);

        String name = certificate.getName();
        if (giftCertificateRepository.findByName(name).isPresent()) {
            throw new EntityAlreadyExistsException();
        }

        certificate.setCreateDate(LocalDateTime.now(UTC));
        certificate.setLastUpdateDate(LocalDateTime.now(UTC));
        long certificateId = giftCertificateRepository.create(certificate);
        certificate.setId(certificateId);

        updateCertificateTagsRepository(certificate, certificateTags);

        return giftCertificateDtoMapper.toDto(certificate, certificateTags);
    }

    @Override
    @Transactional
    public GiftCertificateDto update(GiftCertificateDto certificateDto) {
        GiftCertificate certificate = giftCertificateDtoMapper.toEntity(certificateDto);

        List<ValidationError> validationErrors = giftCertificateValidator.validate(certificate.getName(), certificate.getDescription(),
                certificate.getPrice().toString(), String.valueOf(certificate.getDuration().toDays()),
                certificate.getCreateDate().toString(), certificate.getLastUpdateDate().toString());

        if (!validationErrors.isEmpty()) {
            throw new InvalidEntityException(validationErrors, GiftCertificate.class);
        }

        GiftCertificate storedCertificate;
        long id = certificate.getId();
        Optional<GiftCertificate> optionalCertificate = giftCertificateRepository.findById(id);
        if (!optionalCertificate.isPresent()) {
            throw new EntityAlreadyExistsException();
        }
        else {
            storedCertificate = optionalCertificate.get();
        }
        updateFields(storedCertificate, certificate);

        List<Tag> certificateTags = processTags(certificateDto);

        storedCertificate.setLastUpdateDate(LocalDateTime.now(UTC));
        giftCertificateRepository.update(storedCertificate);

        updateCertificateTagsRepository(storedCertificate, certificateTags);

        return giftCertificateDtoMapper.toDto(storedCertificate, certificateTags);
    }

    @Override
    @Transactional
    public void delete(long id) {
        Optional<GiftCertificate> giftCertificate = giftCertificateRepository.findById(id);
        if (!giftCertificate.isPresent()){
            throw new EntityNotFoundException(id, GiftCertificate.class);
        }
        tagRepository.delete(id);
    }

    private void updateFields(GiftCertificate storedCertificate, GiftCertificate certificate){
        String name = certificate.getName();
        String description = certificate.getDescription();
        BigDecimal price = certificate.getPrice();
        Duration duration = certificate.getDuration();
        LocalDateTime createDate = certificate.getCreateDate();
        LocalDateTime lastUpdateDate = certificate.getLastUpdateDate();

        storedCertificate.setName(name == null ? storedCertificate.getName() : name);
        storedCertificate.setDescription(description == null ? storedCertificate.getDescription() : description);
        storedCertificate.setPrice(price == null ? storedCertificate.getPrice() : price);
        storedCertificate.setDuration(duration == null ? storedCertificate.getDuration() : duration);
        storedCertificate.setCreateDate(createDate == null ? storedCertificate.getCreateDate() : createDate);
        storedCertificate.setLastUpdateDate(lastUpdateDate == null ? storedCertificate.getLastUpdateDate() : lastUpdateDate);
    }

    private List<Tag> processTags(GiftCertificateDto certificateDto){
        List<Tag> certificateTags = certificateDto.getTagsDto()
                .stream()
                .map(tagDtoMapper::toEntity)
                .collect(Collectors.toList());
        for (Tag tag: certificateTags) {
            List<ValidationError> validationErrors = tagValidator.validate(tag.getName());

            if (!validationErrors.isEmpty()) {
                throw new InvalidEntityException(validationErrors, Tag.class);
            }

            Optional<Tag> tagOptional = tagRepository.findByName(tag.getName());

            if (!tagOptional.isPresent()) {
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
