package com.epam.esm.service.impl;

import com.epam.esm.dto.GiftCertificateFilterDto;
import com.epam.esm.dto.GiftCertificateRequestDto;
import com.epam.esm.dto.GiftCertificateResponseDto;
import com.epam.esm.dto.PageDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.mapping.GiftCertificateRequestDtoMapper;
import com.epam.esm.dto.mapping.GiftCertificateResponseDtoMapper;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.exception.EntityNotAvailableException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.InvalidEntityException;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.GiftCertificateSpecificationBuilder;
import com.epam.esm.repository.OrderingType;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.TagSpecificationBuilder;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.validator.GiftCertificateRequestValidator;
import com.epam.esm.validator.ValidationError;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.epam.esm.repository.OrderingType.DESC;
import static com.epam.esm.validator.ValidationError.IMPOSSIBLE_TO_UPDATE_SEVERAL_GIFT_CERTIFICATE_FIELDS;
import static com.epam.esm.validator.ValidationError.NO_GIFT_CERTIFICATE_FIELDS_TO_UPDATE;

@Service
@RequiredArgsConstructor
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private static final String NAME = "name";
    private static final String CREATE_DATE = "createDate";

    private final GiftCertificateRepository giftCertificateRepository;
    private final GiftCertificateRequestValidator giftCertificateRequestValidator;
    private final GiftCertificateRequestDtoMapper giftCertificateRequestDtoMapper;
    private final GiftCertificateResponseDtoMapper giftCertificateResponseDtoMapper;
    private final TagRepository tagRepository;

    @Override
    public Long countAll(GiftCertificateFilterDto filterDto) {

        System.out.println(filterDto.getShowDisabled());
        Specification<GiftCertificate> specification = new GiftCertificateSpecificationBuilder()
                .certificateName(filterDto.getName())
                .certificateAvailability(filterDto.getShowDisabled() ? null : true)
                .certificateDescription(filterDto.getDescription())
                .certificateTagNames(getTags(filterDto.getTagNames()))
                .build();

        return giftCertificateRepository.count(specification);
    }

    @Override
    public List<GiftCertificateResponseDto> findAll(GiftCertificateFilterDto filterDto, PageDto pageDto) {

        Specification<GiftCertificate> specification = new GiftCertificateSpecificationBuilder()
                .certificateName(filterDto.getName())
                .certificateAvailability(filterDto.getShowDisabled() ? null : true)
                .certificateDescription(filterDto.getDescription())
                .certificateTagNames(getTags(filterDto.getTagNames()))
                .build();

        Pageable pageable = createOrderedPageable(filterDto, pageDto);
        return giftCertificateRepository.findAll(specification, pageable)
                .stream()
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
        List<ValidationError> validationErrors = giftCertificateRequestValidator.validateWithRequiredParams(certificateDto);

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

        certificate = giftCertificateRepository.save(certificate);
        return giftCertificateResponseDtoMapper.toDto(certificate);
    }

    @Override
    @Transactional
    public GiftCertificateResponseDto update(long id, GiftCertificateRequestDto certificateDto) {

        GiftCertificate storedCertificate;
        Optional<GiftCertificate> optionalCertificate = giftCertificateRepository.findById(id);
        if (optionalCertificate.isEmpty()) {
            throw new EntityNotFoundException(id, GiftCertificateRequestDto.class);
        }
        else {
            storedCertificate = optionalCertificate.get();
        }

        if (!storedCertificate.getIsAvailable()) {
            throw new EntityNotAvailableException(id, GiftCertificateRequestDto.class);
        }

        int updatedFieldsAmount = countFieldsToUpdate(certificateDto);
        if (updatedFieldsAmount == 0) {
            throw new InvalidEntityException(Collections.singletonList(NO_GIFT_CERTIFICATE_FIELDS_TO_UPDATE), GiftCertificateRequestDto.class);
        }
        if (updatedFieldsAmount > 1) {
            throw new InvalidEntityException(Collections.singletonList(IMPOSSIBLE_TO_UPDATE_SEVERAL_GIFT_CERTIFICATE_FIELDS), GiftCertificateRequestDto.class);
        }

        String priceString = certificateDto.getPrice() == null ? null : certificateDto.getPrice().toString();
        String durationString = certificateDto.getDuration() == null ? null : String.valueOf(certificateDto.getDuration().toDays());
        List<ValidationError> validationErrors = giftCertificateRequestValidator.validateParams(certificateDto.getName(), certificateDto.getDescription(),
                priceString, durationString, certificateDto.getTagIdsDto());

        if (!validationErrors.isEmpty()) {
            throw new InvalidEntityException(validationErrors, GiftCertificateRequestDto.class);
        }

        if (certificateDto.getTagIdsDto() != null) {
            List<Tag> certificateTags = processTagIds(certificateDto.getTagIdsDto());
            storedCertificate.setGiftCertificateTags(certificateTags);
        }

        updateFieldsWithoutTags(storedCertificate, certificateDto);
        storedCertificate = giftCertificateRepository.save(storedCertificate);
        return giftCertificateResponseDtoMapper.toDto(storedCertificate);
    }

    @Override
    @Transactional
    public void disable(long id) {
        Optional<GiftCertificate> giftCertificate = giftCertificateRepository.findById(id);
        if (giftCertificate.isEmpty()){
            throw new EntityNotFoundException(id, GiftCertificateRequestDto.class);
        }
        GiftCertificate storedCertificate = giftCertificate.get();
        storedCertificate.setIsAvailable(false);
        giftCertificateRepository.save(storedCertificate);
    }

    @Override
    @Transactional
    public GiftCertificateResponseDto makeAvailable(long id) {
        Optional<GiftCertificate> giftCertificate = giftCertificateRepository.findById(id);
        if (giftCertificate.isEmpty()){
            throw new EntityNotFoundException(id, GiftCertificateRequestDto.class);
        }
        GiftCertificate storedCertificate = giftCertificate.get();
        storedCertificate.setIsAvailable(true);
        giftCertificateRepository.save(storedCertificate);
        return giftCertificateResponseDtoMapper.toDto(storedCertificate);
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

    private List<Tag> processTagIds(List<Long> certificateTagIds){
        List<Tag> resultCertificateTags = new ArrayList<>();
        for (Long tagId: certificateTagIds) {
            Optional<Tag> tagOptional = tagRepository.findById(tagId);

            if (tagOptional.isPresent()) {
                resultCertificateTags.add(tagOptional.get());
            } else {
                throw new EntityNotFoundException(tagId, TagDto.class);
            }
        }
        return resultCertificateTags;
    }

    private List<Tag> getTags(List<String> tagNames) {
        List<Tag> tags = null;
        if (tagNames != null) {
            tags = tagRepository.findAll(new TagSpecificationBuilder()
                    .tagNames(tagNames)
                    .build());
        }
        return tags;
    }

    private Pageable createOrderedPageable(GiftCertificateFilterDto filterDto, PageDto pageDto) {
        Sort sort = null;
        if (filterDto.getOrderingName() != null) {
            sort = getSort(NAME, filterDto.getOrderingName());
        }

        if (filterDto.getOrderingCreateDate() != null) {
            Sort createDateSort = getSort(CREATE_DATE, filterDto.getOrderingCreateDate());
            if (sort != null) {
                sort = sort.and(createDateSort);
            }
            else {
                sort = createDateSort;
            }
        }

        return sort == null
                ? PageRequest.of(pageDto.getPageNumber() - 1, pageDto.getPageSize())
                : PageRequest.of(pageDto.getPageNumber() - 1, pageDto.getPageSize(), sort);
    }

    private Sort getSort(String orderingColumn, OrderingType orderingType) {
        Sort sort = Sort.by(orderingColumn);
        if (orderingType.equals(DESC)) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }
        return sort;
    }
}
