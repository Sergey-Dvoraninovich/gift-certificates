package com.epam.esm.repository;

import com.epam.esm.TestProfileResolver;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestDatabaseConfig.class)
@ActiveProfiles(resolver = TestProfileResolver.class)
@Transactional
public class TagRepositoryTest {

    @Autowired
    private TagRepository tagRepository;

    @Test
    void testFindByAll() {
        List<Tag> expected = provideTagsList();

        List<Tag> actual;
        actual = tagRepository.findAll();

        assertEquals(expected, actual);
    }

    @Test
    void testFindById() {
        Tag expectedTag = provideTag();
        Optional<Tag> tag = tagRepository.findById(expectedTag.getId());
        boolean result = false;
        if (tag.isPresent()){
            result = tag.get().getId() == expectedTag.getId();
        }
        assertTrue(result);
    }

    @Test
    void testFindByName() {
        Tag expectedTag = provideTag();
        Optional<Tag> tag = tagRepository.findByName(expectedTag.getName());
        boolean result = false;
        if (tag.isPresent()){
            result = tag.get().getId() == expectedTag.getId();
        }
        assertTrue(result);
    }

    @Test
    void testFindByCertificateId() {
        List<Tag> expected = provideCertificateTagsList();

        List<Tag> actual = tagRepository.findByCertificateId(1L);

        assertEquals(expected, actual);
    }

    @Test
    void testCreate() {
        Tag tag = provideTagsList().get(2);
        //new Tag();
        //tag.setName("new tag");

        long generatedId = tagRepository.create(tag);
        boolean result = generatedId > 0;

        assertTrue(result);
    }

    @Test
    void testDelete() {
        Tag tag = provideTag();

        boolean actual = tagRepository.delete(tag.getId());

        assertTrue(actual);
    }

    private Tag provideTag() {
        Tag tag = new Tag();
        tag.setId(1L);
        tag.setName("first tag");
        return tag;
    }

    private List<Tag> provideTagsList() {
        Tag firstTag = new Tag();
        firstTag.setId(1L);
        firstTag.setName("first tag");

        Tag secondTag = new Tag();
        secondTag.setId(2L);
        secondTag.setName("second tag");

        Tag thirdTag = new Tag();
        thirdTag.setId(3L);
        thirdTag.setName("third tag");

        return Arrays.asList(firstTag, secondTag, thirdTag);
    }

    private List<Tag> provideCertificateTagsList() {
        Tag firstTag = new Tag();
        firstTag.setId(1L);
        firstTag.setName("first tag");

        Tag secondTag = new Tag();
        secondTag.setId(2L);
        secondTag.setName("second tag");

        return Arrays.asList(firstTag, secondTag);
    }
}
