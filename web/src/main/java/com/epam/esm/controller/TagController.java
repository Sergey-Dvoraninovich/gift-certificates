package com.epam.esm.controller;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.service.TagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/tags")
public class TagController {
    private TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public ResponseEntity<List<TagDto>> getTags() {
        List<TagDto> tagsDto = tagService.findAll();
        return new ResponseEntity<>(tagsDto, OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagDto> getTag(@PathVariable("id") long id) {
        TagDto tagDto = tagService.findById(id);
        return new ResponseEntity<>(tagDto, OK);
    }

    @PostMapping
    public ResponseEntity<TagDto> createTag(@RequestBody TagDto tagDto) {
        TagDto createdTag = tagService.create(tagDto);
        return new ResponseEntity<>(createdTag, OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable("id") long id) {
        tagService.delete(id);
        return new ResponseEntity<>(NO_CONTENT);
    }
}
