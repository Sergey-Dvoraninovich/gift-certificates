package com.epam.esm.controller;

import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.InvalidPaginationException;
import com.epam.esm.hateos.TagHateoas;
import com.epam.esm.hateos.provider.impl.TagHateoasProvider;
import com.epam.esm.hateos.TagListHateoas;
import com.epam.esm.service.TagService;
import com.epam.esm.validator.PaginationValidator;
import com.epam.esm.validator.ValidationError;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

import static com.epam.esm.validator.ValidationError.PAGE_IS_OUT_OF_RANGE;
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

    @ApiOperation(value = "Get list of Tags", response = TagListHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list of Tags"),
            @ApiResponse(code = 400, message = "The resource can't be fetched due to bad request"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
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
        TagListHateoas tagListHateoas = TagListHateoas.build(tagsDto, tagHateoasProvider,
                tagsDtoAmount, pageNumber, pageSize);
        return new ResponseEntity<>(tagListHateoas, OK);
    }

    @ApiOperation(value = "Get Tag", response = TagHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the Tag"),
            @ApiResponse(code = 400, message = "The Tag can't be fetched due to bad request"),
            @ApiResponse(code = 404, message = "The Tag you were trying to reach is not found")
    }
    )
    @GetMapping("/{id}")
    public ResponseEntity<TagHateoas> getTag(@ApiParam(value = "The Tag ID") @PathVariable("id") @Min(1) long id) {
        TagDto tagDto = tagService.findById(id);
        TagHateoas tagHateoas = TagHateoas.build(tagDto, tagHateoasProvider);
        return new ResponseEntity<>(tagHateoas, OK);
    }

    @ApiOperation(value = "Create Tag", response = TagHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created a Tag"),
            @ApiResponse(code = 400, message = "The Tag can't be created due to bad request"),
            @ApiResponse(code = 409, message = "The Tag can't be created due to a conflict with the current state of the resource.")
    }
    )
    @PostMapping
    public ResponseEntity<TagHateoas> createTag(@ApiParam(value = "The Tag create request dto") @RequestBody @NotNull TagDto tagDto) {
        TagDto createdTag = tagService.create(tagDto);
        TagHateoas tagHateoas = TagHateoas.build(createdTag, tagHateoasProvider);
        return new ResponseEntity<>(tagHateoas, CREATED);
    }

    @ApiOperation(value = "Delete Tag")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successfully deleted a Tag"),
            @ApiResponse(code = 400, message = "The Tag can't be deleted due to bad request"),
            @ApiResponse(code = 404, message = "The Tag is not found to be deleted")
    }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@ApiParam(value = "The Tag ID") @PathVariable("id") @Min(1) long id) {
        tagService.delete(id);
        return new ResponseEntity<>(NO_CONTENT);
    }
}
