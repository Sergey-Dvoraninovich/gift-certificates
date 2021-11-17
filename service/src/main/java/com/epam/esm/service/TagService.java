package com.epam.esm.service;

import com.epam.esm.dto.TagDto;

import java.util.List;

/**
 * The interface Tag service.
 */
public interface TagService {

    Long countAll();

    List<TagDto> findAll(Integer pageNumber, Integer pageSize);

    /**
     * Find by TagDto id.
     *
     * @param id the id of TagDto
     * @return the TagDto
     */
    TagDto findById(long id);

    /**
     * Find by TagDto name.
     *
     * @param name the name of TagDto
     * @return the TagDto
     */
    TagDto findByName(String name);

    /**
     * Create TagDto.
     *
     * @param tagDto the TagDto
     * @return the created TagDto
     */
    TagDto create(TagDto tagDto);

    /**
     * Delete TagDto.
     *
     * @param id the id of TagDto
     */
    void delete(long id);
}
