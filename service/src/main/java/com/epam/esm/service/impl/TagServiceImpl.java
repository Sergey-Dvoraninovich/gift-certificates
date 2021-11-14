package com.epam.esm.service.impl;

import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.mapping.TagDtoMapper;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.InvalidEntityException;
import com.epam.esm.handler.PaginationHandler;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.TagService;
import com.epam.esm.validator.TagValidator;
import com.epam.esm.validator.ValidationError;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;
    private final TagValidator tagValidator;
    private final TagDtoMapper tagMapper;
    private final PaginationHandler paginationHandler;

    @Override
    public List<TagDto> findAll(Integer pageNumber, Integer pageSize){
        paginationHandler.handle(pageNumber, pageSize);
        int minPos = paginationHandler.getMinPos();
        int maxPos = paginationHandler.getMaxPos();
        return tagRepository.findAll(minPos, maxPos)
                .stream()
                .map(tagMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public TagDto findById(long id){
        Optional<Tag> tag = tagRepository.findById(id);
        if (tag.isPresent()) {
            return tagMapper.toDto(tag.get());
        }
        else {
            throw new EntityNotFoundException(id, TagDto.class);
        }
    }

    @Override
    public TagDto findByName(String name){
        Optional<Tag> tag = tagRepository.findByName(name);
        if (tag.isPresent()) {
            return tagMapper.toDto(tag.get());
        }
        else {
            throw new EntityNotFoundException(TagDto.class);
        }
    }

    @Transactional
    @Override
    public TagDto create(TagDto tagDto){
        Tag tag = tagMapper.toEntity(tagDto);
        List<ValidationError> validationErrors = tagValidator.validateWithRequiredParams(tagDto);

        if (!validationErrors.isEmpty()) {
            throw new InvalidEntityException(validationErrors, TagDto.class);
        }

        String name = tag.getName();
        if (tagRepository.findByName(name).isPresent()) {
            throw new EntityAlreadyExistsException(TagDto.class);
        }

        tag = tagRepository.create(tag);
        return tagMapper.toDto(tag);
    }

    @Transactional
    @Override
    public void delete(long id) {
        Optional<Tag> tag = tagRepository.findById(id);
        if (!tag.isPresent()){
            throw new EntityNotFoundException(id, TagDto.class);
        }
        tagRepository.delete(tag.get());
    }
}
