package com.epam.esm.controller;

import com.epam.esm.dto.TagDto;
import com.epam.esm.handler.PaginationHandler;
import com.epam.esm.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.websocket.server.PathParam;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;
    private final PaginationHandler paginationHandler;

    @GetMapping
    public ResponseEntity<List<TagDto>> getTags(@PathParam("pageNumber") Integer pageNumber,
                                                @PathParam("pageSize") Integer pageSize) {
        List<TagDto> tagsDto = tagService.findAll(pageNumber, pageSize);
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
        return new ResponseEntity<>(createdTag, CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable("id") long id) {
        tagService.delete(id);
        return new ResponseEntity<>(NO_CONTENT);
    }
}
