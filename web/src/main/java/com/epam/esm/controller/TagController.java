package com.epam.esm.controller;

import com.epam.esm.dto.TagDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Map;

@RequestMapping("/api/v1/tags")
public interface TagController extends PagedController<TagDto> {

    @ApiOperation(value = "Get list of Tags", response = PagedModel.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list of Tags"),
            @ApiResponse(code = 400, message = "The resource can't be fetched due to bad request"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    ResponseEntity<PagedModel<TagDto>> getAll(@ApiParam(value = "The Tag params") @RequestParam Map<String, Object> params);

    @ApiOperation(value = "Get Tag", response = TagDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the Tag"),
            @ApiResponse(code = 400, message = "The Tag can't be fetched due to bad request"),
            @ApiResponse(code = 404, message = "The Tag you were trying to reach is not found")
    }
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    ResponseEntity<TagDto> getTag(@ApiParam(value = "The Tag ID") @PathVariable("id") @Min(1) long id) ;


    @ApiOperation(value = "Create Tag", response = TagDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created a Tag"),
            @ApiResponse(code = 400, message = "The Tag can't be created due to bad request"),
            @ApiResponse(code = 409, message = "The Tag can't be created due to a conflict with the current state of the resource.")
    }
    )
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    ResponseEntity<TagDto> createTag(@ApiParam(value = "The Tag create request dto") @RequestBody @NotNull TagDto tagDto);

    @ApiOperation(value = "Delete Tag")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successfully deleted a Tag"),
            @ApiResponse(code = 400, message = "The Tag can't be deleted due to bad request"),
            @ApiResponse(code = 404, message = "The Tag is not found to be deleted")
    }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    ResponseEntity<Void> deleteTag(@ApiParam(value = "The Tag ID") @PathVariable("id") @Min(1) long id);

    @ApiOperation(value = "Get most widely used tag for user with highest order price", response = TagDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved tag"),
            @ApiResponse(code = 400, message = "The tag can't be fetched due to bad request"),
            @ApiResponse(code = 404, message = "The tag you were trying to reach is not found")
    }
    )
    @GetMapping("/mostWidelyUsedTag")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    ResponseEntity<TagDto> getMostWidelyUsedTag();

}
