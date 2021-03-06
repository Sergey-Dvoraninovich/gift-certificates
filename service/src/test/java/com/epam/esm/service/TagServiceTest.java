package com.epam.esm.service;

import com.epam.esm.dto.PageDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.mapping.TagDtoMapper;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.impl.TagServiceImpl;
import com.epam.esm.validator.TagValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {
    private static final Integer PAGE_NUMBER = 1;
    private static final Integer PAGE_SIZE = 10;

    @InjectMocks
    private TagServiceImpl tagService;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private TagValidator tagValidator;

    @Mock
    private TagDtoMapper tagDtoMapper;

    @BeforeAll
    static void setUp() {
        MockitoAnnotations.openMocks(TagServiceTest.class);
    }

    @Test
    void testFindAll() {
        List<Tag> tags = provideTagsList();
        List<TagDto> tagsDto = provideTagsDtoList();
        Page<Tag> tagsPage = new PageImpl<>(tags);
        when(tagRepository.findAll(PageRequest.of(PAGE_NUMBER - 1, PAGE_SIZE))).thenReturn(tagsPage);
        for (int i = 0; i < tags.size(); i++) {
            when(tagDtoMapper.toDto(tags.get(i))).thenReturn(tagsDto.get(i));
        }

        List<TagDto> expectedDtoList = provideTagsDtoList();
        List<TagDto> actualDtoList = tagService.findAll(new PageDto(PAGE_NUMBER, PAGE_SIZE));

        assertEquals(expectedDtoList, actualDtoList);
    }

    @Test
    void testFindById() {
        Tag tag = provideTag();
        when(tagRepository.findById(tag.getId())).thenReturn(Optional.of(tag));
        TagDto tagDto = provideTagDto();
        when(tagDtoMapper.toDto(tag)).thenReturn(tagDto);

        TagDto actual = tagService.findById(tag.getId());

        assertEquals(tagDto, actual);
    }

    @Test
    void testFindByName() {
        Tag tag = provideTag();
        when(tagRepository.findTagByName(tag.getName())).thenReturn(Optional.of(tag));
        TagDto tagDto = provideTagDto();
        when(tagDtoMapper.toDto(tag)).thenReturn(tagDto);

        TagDto actual = tagService.findByName(tag.getName());

        assertEquals(tagDto, actual);
    }

    @Test
    void testCreate() {
        Tag tag = provideTag();
        when(tagRepository.findTagByName(tag.getName())).thenReturn(Optional.empty());
        when(tagRepository.save(tag)).thenReturn(tag);
        TagDto tagDto = provideTagDto();
        when(tagDtoMapper.toDto(tag)).thenReturn(tagDto);
        when(tagDtoMapper.toEntity(tagDto)).thenReturn(tag);
        when(tagValidator.validateWithRequiredParams(tagDto)).thenReturn(new ArrayList<>());

        TagDto actual = tagService.create(tagDto);

        assertEquals(tagDto, actual);
    }

    @Test
    void testDelete() {
        Tag tag = provideTag();
        when(tagRepository.findById(tag.getId())).thenReturn(Optional.of(tag));

        tagService.delete(tag.getId());

        assertDoesNotThrow(() -> new EntityNotFoundException(tag.getId(), TagDto.class));
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

    private Tag provideTag() {
        Tag firstTag = new Tag();
        firstTag.setId(1L);
        firstTag.setName("first tag");
        return firstTag;
    }

    private List<TagDto> provideTagsDtoList() {
        TagDto firstTagDto = new TagDto();
        firstTagDto.setId(1L);
        firstTagDto.setName("first tag");

        TagDto secondTagDto = new TagDto();
        secondTagDto.setId(2L);
        secondTagDto.setName("second tag");

        TagDto thirdTagDto = new TagDto();
        thirdTagDto.setId(3L);
        thirdTagDto.setName("third tag");

        return Arrays.asList(firstTagDto, secondTagDto, thirdTagDto);
    }

    private TagDto provideTagDto() {
        TagDto firstTag = new TagDto();
        firstTag.setId(1L);
        firstTag.setName("first tag");
        return firstTag;
    }
}
