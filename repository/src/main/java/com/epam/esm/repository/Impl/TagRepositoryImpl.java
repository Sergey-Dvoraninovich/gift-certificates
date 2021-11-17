package com.epam.esm.repository.Impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TagRepositoryImpl implements TagRepository {
    private static final String TAG_NAME_PARAM = "tagName";

    private static final String COUNT_ALL_TAGS
            = "SELECT COUNT(t) FROM Tag t";

    private static final String ALL_TAGS
            = "SELECT t FROM Tag t";

    private static final String TAG_BY_NAME
            = "SELECT t FROM Tag t WHERE t.name = :tagName";

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Long countAll() {
        return (Long) entityManager.createQuery(COUNT_ALL_TAGS)
                .getSingleResult();
    }

    @Override
    public List<Tag> findAll(int pageNumber, int pageSize) {
        return entityManager.createQuery(ALL_TAGS, Tag.class)
                .setFirstResult((pageNumber - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    @Override
    public Optional<Tag> findById(long id) {
        Tag tag = entityManager.find(Tag.class, id);
        return Optional.ofNullable(tag);
    }

    @Override
    public Optional<Tag> findByName(String name) {
        List<Tag> tags = entityManager.createQuery(TAG_BY_NAME, Tag.class)
                .setParameter(TAG_NAME_PARAM, name)
                .getResultList();
        return tags.size() == 0 ? Optional.empty() : Optional.of(tags.get(0));
    }

    @Override
    public Tag create(Tag tag) {
        entityManager.persist(tag);
        return tag;
    }

    @Override
    public boolean delete(Tag tag) {
        entityManager.remove(tag);
        return entityManager.find(Tag.class, tag.getId()) == null;
    }
}
