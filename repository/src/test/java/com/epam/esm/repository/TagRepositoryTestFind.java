package com.epam.esm.repository;

import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class TagRepositoryTestFind {
    private static final Integer PAGE_NUMBER = 0;
    private static final Integer PAGE_SIZE = 10;

    @Autowired
    private TagRepository tagRepository;

    @Test
    void testFindById() {
        //Given
        Tag expected = provideNewTag("new tag");

        //When
        Optional<Tag> actualOptional = tagRepository.findById(expected.getId());
        Tag actual = actualOptional.orElseGet(null);

        //Then
        assertNotNull(actual);
        assertEquals(expected, actual);

        //Clean
        removeRedundantTag(actual);
    }

    @Test
    void testFindByName() {
        //Given
        Tag expected = provideNewTag("new tag");

        //When
        Optional<Tag> actualOptional = tagRepository.findTagByName(expected.getName());
        Tag actual = actualOptional.orElseGet(null);

        //Then
        assertNotNull(actual);
        assertEquals(expected, actual);

        //Clean
        removeRedundantTag(actual);
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
            oldTagOptional.ifPresent(tag -> tagRepository.delete(tag));
        });

        //Create new tags
        List<String> tagNameList = Arrays.asList("first new tag", "second new tag", "third new tag");
        return tagNameList.stream().map(this::provideNewTag).collect(Collectors.toList());
    }
}
