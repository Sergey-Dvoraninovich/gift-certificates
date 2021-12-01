package com.epam.esm.repository;

import com.epam.esm.TestProfileResolver;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = TestDatabaseConfig.class)
@ActiveProfiles(resolver = TestProfileResolver.class)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {"classpath:init_data.sql"})
public class TagRepositoryTest {
    private static final Integer PAGE_NUMBER = 1;
    private static final Integer PAGE_SIZE = 10;

    @Autowired
    private TagRepository tagRepository;

    @Test
    void testCountAll() {
        List<Tag> expected = provideTagsList();

        long actual = tagRepository.countAll();

        assertEquals(expected.size(), actual);
    }

    @Test
    void testFindAll() {
        List<Tag> expected = provideTagsList();

        List<Tag> actual;
        actual = tagRepository.findAll(PAGE_NUMBER, PAGE_SIZE);

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

    //TODO work with it
//    @Test
//    @Transactional
//    void testCreate() {
//        Tag newTag = new Tag();
//        newTag.setName("new tag");
//
//        long generatedId = tagRepository.create(newTag).getId();
//        boolean result = generatedId > 0;
//
//        assertTrue(result);
//    }

    //TODO work with it
//    @Test
//    @Transactional
//    void testDelete() {
//        Tag tag = provideTag();
//
//        boolean actual = tagRepository.delete(tag);
//
//        assertTrue(actual);
//    }

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
}
