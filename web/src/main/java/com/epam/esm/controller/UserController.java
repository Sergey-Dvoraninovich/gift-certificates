package com.epam.esm.controller;

import com.epam.esm.dto.UserDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.Min;
import java.util.Map;

@RequestMapping("/api/v1/users")
public interface UserController extends PagedController<UserDto> {

    @ApiOperation(value = "Get list of Users", response = PagedModel.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list of Users"),
            @ApiResponse(code = 400, message = "The resource can't be fetched due to bad request"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    ResponseEntity<PagedModel<UserDto>> getAll(@ApiParam(value = "The User params", required = false) @RequestParam Map<String, Object> params);

    @ApiOperation(value = "Get User", response = UserDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the User"),
            @ApiResponse(code = 400, message = "The User can't be fetched due to bad request"),
            @ApiResponse(code = 404, message = "The User you were trying to reach is not found")
    }
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    ResponseEntity<UserDto> getUser(@ApiParam(value = "The User ID") @PathVariable("id") @Min(1) long id);
}
