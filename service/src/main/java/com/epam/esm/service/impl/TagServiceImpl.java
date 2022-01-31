package com.epam.esm.service.impl;

import com.epam.esm.dto.PageDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.mapping.TagDtoMapper;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.InvalidEntityException;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.TagService;
import com.epam.esm.validator.TagValidator;
import com.epam.esm.validator.ValidationError;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;
    private final TagValidator tagValidator;
    private final TagDtoMapper tagDtoMapper;

    @Override
    public Long countAll() {
        return tagRepository.count();
    }

    @Override
    public List<TagDto> findAll(PageDto pageDto){
        return tagRepository.findAll(pageDto.toPageable())
                .stream()
                .map(tagDtoMapper::toDto)
                .toList();
    }

    @Override
    public TagDto findById(long id){
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, TagDto.class));
        return tagDtoMapper.toDto(tag);
    }

    @Override
    public TagDto findByName(String name){
        Tag tag = tagRepository.findTagByName(name)
                .orElseThrow(() -> new EntityNotFoundException(TagDto.class));
        return tagDtoMapper.toDto(tag);
    }

    @Transactional
    @Override
    public TagDto create(TagDto tagDto){
        Tag tag = tagDtoMapper.toEntity(tagDto);
        List<ValidationError> validationErrors = tagValidator.validateWithRequiredParams(tagDto);

        if (!validationErrors.isEmpty()) {
            throw new InvalidEntityException(validationErrors, TagDto.class);
        }

        String name = tag.getName();
        if (tagRepository.findTagByName(name).isPresent()) {
            throw new EntityAlreadyExistsException(TagDto.class);
        }

        tag = tagRepository.save(tag);
        return tagDtoMapper.toDto(tag);
    }

    @Transactional
    @Override
    public void delete(long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, TagDto.class));
        tagRepository.delete(tag);
    }

    @Override
    public TagDto findMostWidelyUsedTag() {
        List<Order> orders = tagRepository.findOrdersHighestCoast();
        if (orders.isEmpty()) {
            throw new EntityNotFoundException(TagDto.class);
        }

        long userId = orders.get(0).getUser().getId();
        List<Tag> tags = tagRepository.findMostWidelyUsedUserTags(userId);
        if (tags.isEmpty()) {
            throw new EntityNotFoundException(TagDto.class);
        }

        return tagDtoMapper.toDto(tags.get(0));
    }
}
