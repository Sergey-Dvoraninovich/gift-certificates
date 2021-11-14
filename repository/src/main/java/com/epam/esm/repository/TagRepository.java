package com.epam.esm.repository;

import com.epam.esm.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagRepository {

    List<Tag> findAll(int minPos, int maxPos);
    Optional<Tag> findById(long id);
    Optional<Tag> findByName(String name);

    Tag create(Tag tag);
    boolean delete(Tag tag);
}
