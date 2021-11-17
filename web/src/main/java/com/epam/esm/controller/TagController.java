package com.epam.esm.controller;

import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.InvalidPaginationException;
import com.epam.esm.hateos.TagHateoas;
import com.epam.esm.hateos.provider.impl.TagHateoasProvider;
import com.epam.esm.hateos.TagListHateoas;
import com.epam.esm.service.TagService;
import com.epam.esm.validator.PaginationValidator;
import com.epam.esm.validator.ValidationError;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.validator.ValidationError.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;
    private final TagHateoasProvider tagHateoasProvider;
    private final PaginationValidator paginationValidator;

    @GetMapping
    public ResponseEntity<TagListHateoas> getTags(@RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
                                                  @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        List<ValidationError> validationErrors = paginationValidator.validateParams(pageNumber, pageSize);
        if (!validationErrors.isEmpty()) {
            throw new InvalidPaginationException(pageNumber, pageSize, validationErrors);
        }

        Long tagsDtoAmount = tagService.countAll();
        if (tagsDtoAmount <= (pageNumber - 1) * pageSize) {
            throw new InvalidPaginationException(pageNumber, pageSize, Collections.singletonList(PAGE_IS_OUT_OF_RANGE));
        }

        List<TagDto> tagsDto = tagService.findAll(pageNumber, pageSize);
        TagListHateoas tagListHateoas = TagListHateoas.build(tagsDto, tagHateoasProvider);
        if (pageNumber > 1) {
            Link prevLink = linkTo(methodOn(TagController.class).getTags(pageNumber - 1, pageSize)).withRel("prevPage");
            tagListHateoas.add(prevLink);
        }
        if (tagsDtoAmount > pageNumber * pageSize) {
            Link nextLink = linkTo(methodOn(TagController.class).getTags(pageNumber + 1, pageSize)).withRel("nextPage");
            tagListHateoas.add(nextLink);
        }
        return new ResponseEntity<>(tagListHateoas, OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagHateoas> getTag(@PathVariable("id") long id) {
        TagDto tagDto = tagService.findById(id);
        TagHateoas tagHateoas = TagHateoas.build(tagDto, tagHateoasProvider);
        return new ResponseEntity<>(tagHateoas, OK);
    }

    @PostMapping
    public ResponseEntity<TagHateoas> createTag(@RequestBody TagDto tagDto) {
        TagDto createdTag = tagService.create(tagDto);
        TagHateoas tagHateoas = TagHateoas.build(createdTag, tagHateoasProvider);
        return new ResponseEntity<>(tagHateoas, CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable("id") long id) {
        tagService.delete(id);
        return new ResponseEntity<>(NO_CONTENT);
    }
}
