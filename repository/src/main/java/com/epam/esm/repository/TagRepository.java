package com.epam.esm.repository;

import com.epam.esm.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagRepository {
    Long countAll();

    List<Tag> findAll(int pageNumber, int pageSize);
    Optional<Tag> findById(long id);
    Optional<Tag> findByName(String name);

    Tag create(Tag tag);
    boolean delete(Tag tag);

    Optional<Tag> findMostWidelyUsedTag();
}
