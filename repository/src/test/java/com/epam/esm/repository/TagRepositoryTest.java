package com.epam.esm.repository;

import com.epam.esm.TestProfileResolver;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TestDatabaseConfig.class)
@ActiveProfiles(resolver = TestProfileResolver.class)
@Transactional
public class TagRepositoryTest {
    private static final Integer PAGE_NUMBER = 1;
    private static final Integer PAGE_SIZE = 10;

    @Autowired
    private TagRepository tagRepository;

    @Test
    void countAll() {
        //Given
        List<Tag> expected = provideNewTagsList();

        //When
        long actual = tagRepository.count();

        //Then
        assertEquals(expected.size(), actual);

        //Clean
        expected.forEach(tag -> removeRedundantTag(tag));
    }

    @Test
    void testFindAll() {
        //Given
        List<Tag> expected = provideNewTagsList();

        //When
        List<Tag> actual = (List<Tag>) tagRepository.findAll(PageRequest.of(PAGE_NUMBER, PAGE_SIZE));

        //Then
        assertNotNull(actual);
        assertEquals(expected, actual);

        //Clean
        expected.forEach(tag -> removeRedundantTag(tag));
    }

    @Test
    void testFindById() {
        //Given
        Tag expectedTag = provideNewTag("new tag");

        //When
        Optional<Tag> actualTagOptional = tagRepository.findById(expectedTag.getId());
        Tag actualTag = actualTagOptional.orElseGet(null);

        //Then
        assertNotNull(actualTag);
        assertEquals(expectedTag.getId(), actualTag.getId());
        assertEquals(expectedTag.getName(), actualTag.getName());

        //Clean
        removeRedundantTag(actualTag);
    }



    @Test
    void testFindByName() {
        //Given
        Tag expectedTag = provideNewTag("new tag");

        //When
        Optional<Tag> actualTagOptional = tagRepository.findTagByName(expectedTag.getName());
        Tag actualTag = actualTagOptional.orElseGet(null);

        //Then
        assertNotNull(actualTag);
        assertEquals(expectedTag.getId(), actualTag.getId());
        assertEquals(expectedTag.getName(), actualTag.getName());

        //Clean
        removeRedundantTag(actualTag);
    }

    @Test
    void testCreate() {
        //Given
        Tag newTag = new Tag();
        newTag.setName("new tag");

        //When
        long generatedId = tagRepository.save(newTag).getId();

        //Then
        assertTrue(generatedId > 0);

        //Clean
        removeRedundantTag(newTag);
    }



    @Test
    void testDelete() {
        //Given
        Tag tag = provideNewTag("new tag");

        //When
        tagRepository.delete(tag);

        //Then
        Optional<Tag> deletedTag = tagRepository.findById(tag.getId());
        assertFalse(deletedTag.isPresent());
    }



    private Tag provideNewTag(String tagName) {
        Tag newTag = new Tag();
        newTag.setName(tagName);

        long generatedId = tagRepository.save(newTag).getId();
        assertTrue(generatedId > 0);

        return newTag;
    }

    private void removeRedundantTag(Tag tag) {
        tagRepository.delete(tag);
    }

    private List<Tag> provideNewTagsList() {
        //Remove old tags form DB.
        Arrays.asList("first tag", "second tag", "third tag").forEach(tagName -> {
            Optional<Tag> oldTagOptional = tagRepository.findTagByName(tagName);
            if (oldTagOptional.isPresent()) {
                tagRepository.delete(oldTagOptional.get());
            }});

        //Create new tags
        List<String> tagNameList = Arrays.asList("first new tag", "second new tag", "third new tag");
        return tagNameList.stream().map(tagName -> provideNewTag(tagName)).collect(Collectors.toList());
    }

}
