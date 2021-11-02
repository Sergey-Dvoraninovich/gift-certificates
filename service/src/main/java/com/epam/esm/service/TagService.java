package com.epam.esm.service;

import com.epam.esm.dto.TagDto;

import java.util.List;

public interface TagService {
    List<TagDto> findAll();
    TagDto findById(long id);
    TagDto findByName(String name);

    TagDto create(TagDto tagDto);
    void delete(long id);
}
