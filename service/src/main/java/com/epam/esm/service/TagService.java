package com.epam.esm.service;

import com.epam.esm.dto.TagDto;

import java.util.List;

public interface TagService {

    Long countAll();

    List<TagDto> findAll(Integer pageNumber, Integer pageSize);

    TagDto findById(long id);

    TagDto findByName(String name);

    TagDto create(TagDto tagDto);

    void delete(long id);

    TagDto findMostWidelyUsedTag();
}
