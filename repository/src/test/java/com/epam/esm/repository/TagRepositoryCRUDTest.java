package com.epam.esm.repository;

import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class TagRepositoryCRUDTest {

    @Autowired
    private TagRepository tagRepository;

    @Test
    void testCreate() {
        //Given
        Tag newTag = new Tag();
        newTag.setName("new tag");

        //When
        Tag actual = tagRepository.save(newTag);

        //Then
        assertNotNull(actual);
        assertTrue(actual.getId() > 0);

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

}
