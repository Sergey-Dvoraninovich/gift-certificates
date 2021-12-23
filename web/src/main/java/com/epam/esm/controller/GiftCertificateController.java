package com.epam.esm.controller;

import com.epam.esm.dto.GiftCertificateRequestDto;
import com.epam.esm.dto.GiftCertificateResponseDto;
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

@RequestMapping("/api/v1/giftCertificates")
public interface GiftCertificateController extends PagedController<GiftCertificateResponseDto> {

    @ApiOperation(value = "Get list of GiftCertificates", response = PagedModel.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list of GiftCertificates"),
            @ApiResponse(code = 400, message = "The resource can't be fetched due to bad request"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @GetMapping
    ResponseEntity<PagedModel<GiftCertificateResponseDto>> getAll(@ApiParam(value = "The Gift Certificate params") @RequestParam Map<String, Object> params);

    @ApiOperation(value = "Get GiftCertificate", response = GiftCertificateResponseDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the GiftCertificate"),
            @ApiResponse(code = 400, message = "The GiftCertificate can't be fetched due to bad request"),
            @ApiResponse(code = 404, message = "The GiftCertificate you were trying to reach is not found")
    }
    )
    @GetMapping("/{id}")
    ResponseEntity<GiftCertificateResponseDto> getGiftCertificate(@ApiParam(value = "The GiftCertificate ID") @PathVariable("id") @Min(1) long id);

    @ApiOperation(value = "Create GiftCertificate", response = GiftCertificateResponseDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created a GiftCertificate"),
            @ApiResponse(code = 400, message = "The GiftCertificate can't be created due to bad request"),
            @ApiResponse(code = 409, message = "The GiftCertificate can't be created due to a conflict with the current state of the resource.")
    }
    )
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    ResponseEntity<GiftCertificateResponseDto> createGiftCertificate(@ApiParam(value = "The GiftCertificate create request dto") @RequestBody @NotNull GiftCertificateRequestDto giftCertificateDto);

    @ApiOperation(value = "Update GiftCertificate", response = GiftCertificateResponseDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated a GiftCertificate"),
            @ApiResponse(code = 400, message = "The GiftCertificate can't be updated due to bad request"),
            @ApiResponse(code = 409, message = "The GiftCertificate can't be updated due to a conflict with the current state of the resource.")
    }
    )
    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    ResponseEntity<GiftCertificateResponseDto> updateGiftCertificate(@ApiParam(value = "The GiftCertificate ID") @PathVariable("id") @Min(1) long id,
                                                                     @ApiParam(value = "The GiftCertificate update request dto") @RequestBody @NotNull GiftCertificateRequestDto giftCertificate);

    @ApiOperation(value = "Delete GiftCertificate")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successfully deleted a GiftCertificate"),
            @ApiResponse(code = 400, message = "The GiftCertificate can't be deleted due to bad request"),
            @ApiResponse(code = 404, message = "The GiftCertificate is not found to be deleted")
    }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    ResponseEntity<Void> deleteGiftCertificate(@ApiParam(value = "The GiftCertificate ID") @PathVariable("id") @Min(1) long id);
}
