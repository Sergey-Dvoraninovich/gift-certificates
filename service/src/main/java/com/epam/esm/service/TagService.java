package com.epam.esm.service;

import com.epam.esm.dto.TagDto;

import java.util.List;

/**
 * The interface Tag service.
 */
public interface TagService {

    /**
     * Count amount of all TAgs.
     *
     * @return the long
     */
    Long countAll();

    /**
     * Find all Tags.
     *
     * @param pageNumber the page number
     * @param pageSize   the page size
     * @return the list of Tags
     */
    List<TagDto> findAll(Integer pageNumber, Integer pageSize);

    /**
     * Find Tag by id.
     *
     * @param id the Tag id
     * @return the Tag Dto
     */
    TagDto findById(long id);

    /**
     * Find Tag by name.
     *
     * @param name the Tag name
     * @return the Tag Dto
     */
    TagDto findByName(String name);

    /**
     * Create Tag.
     *
     * @param tagDto the Tag Dto
     * @return the Tag Dto
     */
    TagDto create(TagDto tagDto);

    /**
     * Delete Tag.
     *
     * @param id the Tag id
     */
    void delete(long id);

    /**
     * Find most widely used tag Tag of User with highest order coast
     *
     * @return the Tag Dto
     */
    TagDto findMostWidelyUsedTag();
}
