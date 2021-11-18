package com.epam.esm.controller;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.OrderCreateRequestDto;
import com.epam.esm.dto.OrderResponseDto;
import com.epam.esm.exception.InvalidPaginationException;
import com.epam.esm.hateos.GiftCertificateHateoas;
import com.epam.esm.hateos.GiftCertificateListHateoas;
import com.epam.esm.hateos.provider.impl.GiftCertificateHateoasProvider;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.validator.PaginationValidator;
import com.epam.esm.validator.ValidationError;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

import static com.epam.esm.validator.ValidationError.PAGE_IS_OUT_OF_RANGE;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/giftCertificates")
@RequiredArgsConstructor
@Validated
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;
    private final GiftCertificateHateoasProvider giftCertificateHateoasProvider;
    private final PaginationValidator paginationValidator;

    @ApiOperation(value = "Get list of GiftCertificates", response = GiftCertificateListHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list of GiftCertificates"),
            @ApiResponse(code = 400, message = "The resource can't be fetched due to bad request"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @GetMapping
    public ResponseEntity<GiftCertificateListHateoas> getGiftCertificates(@ApiParam(value = "tagName", required = false) @RequestParam(value = "tagName", required = false) String[] tagNames,
                                                                          @ApiParam(value = "certificateName", required = false) @RequestParam(value ="certificateName", required = false) String certificateName,
                                                                          @ApiParam(value = "orderingName", required = false) @RequestParam(value ="orderingName", required = false) String orderingName,
                                                                          @ApiParam(value = "certificateDescription", required = false) @RequestParam(value ="certificateDescription", required = false) String certificateDescription,
                                                                          @ApiParam(value = "orderingDate", required = false) @RequestParam(value = "orderingDate", required = false) String orderingDate,
                                                                          @ApiParam(value = "pageNumber", required = false) @RequestParam(value = "pageNumber", defaultValue = "1") @Min(1) Integer pageNumber,
                                                                          @ApiParam(value = "pageNumber", required = false) @RequestParam(value = "pageNumber", defaultValue = "10") @Min(1) Integer pageSize) {

        List<ValidationError> validationErrors = paginationValidator.validateParams(pageNumber, pageSize);
        if (!validationErrors.isEmpty()) {
            throw new InvalidPaginationException(pageNumber, pageSize, validationErrors);
        }

        Long certificatesDtoAmount = giftCertificateService.countAll(tagNames, certificateName, orderingName,
                certificateDescription, orderingDate);
        if (certificatesDtoAmount <= (pageNumber - 1) * pageSize) {
            throw new InvalidPaginationException(pageNumber, pageSize, Collections.singletonList(PAGE_IS_OUT_OF_RANGE));
        }

        List<GiftCertificateDto> giftCertificatesDto = giftCertificateService.findAll(tagNames, certificateName, orderingName,
                certificateDescription, orderingDate,
                pageNumber, pageSize);
        GiftCertificateListHateoas certificateListHateoas = GiftCertificateListHateoas.build(giftCertificatesDto, giftCertificateHateoasProvider,
                tagNames, certificateName, orderingName, certificateDescription, orderingDate,
                certificatesDtoAmount, pageNumber, pageSize);
        return new ResponseEntity<>(certificateListHateoas, OK);
    }

    @ApiOperation(value = "Get GiftCertificate", response = GiftCertificateHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the GiftCertificate"),
            @ApiResponse(code = 400, message = "The GiftCertificate can't be fetched due to bad request"),
            @ApiResponse(code = 404, message = "The GiftCertificate you were trying to reach is not found")
    }
    )
    @GetMapping("/{id}")
    public ResponseEntity<GiftCertificateHateoas> getGiftCertificate(@ApiParam(value = "The GiftCertificate ID") @PathVariable("id") @Min(1) long id) {
        GiftCertificateDto giftCertificateDto = giftCertificateService.findById(id);
        GiftCertificateHateoas certificateHateoas = GiftCertificateHateoas.build(giftCertificateDto, giftCertificateHateoasProvider);
        return new ResponseEntity<>(certificateHateoas, OK);
    }

    @ApiOperation(value = "Create GiftCertificate", response = GiftCertificateHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created a GiftCertificate"),
            @ApiResponse(code = 400, message = "The GiftCertificate can't be created due to bad request"),
            @ApiResponse(code = 409, message = "The GiftCertificate can't be created due to a conflict with the current state of the resource.")
    }
    )
    @PostMapping
    public ResponseEntity<GiftCertificateHateoas> createGiftCertificate(@ApiParam(value = "The GiftCertificate create request dto") @RequestBody @NotNull GiftCertificateDto giftCertificateDto) {
        GiftCertificateDto createdGiftCertificate = giftCertificateService.create(giftCertificateDto);
        GiftCertificateHateoas certificateHateoas = GiftCertificateHateoas.build(createdGiftCertificate, giftCertificateHateoasProvider);
        return new ResponseEntity<>(certificateHateoas, CREATED);
    }

    @ApiOperation(value = "Update GiftCertificate", response = GiftCertificateHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated a GiftCertificate"),
            @ApiResponse(code = 400, message = "The GiftCertificate can't be updated due to bad request"),
            @ApiResponse(code = 409, message = "The GiftCertificate can't be updated due to a conflict with the current state of the resource.")
    }
    )
    @PatchMapping("/{id}")
    public ResponseEntity<GiftCertificateHateoas> updateGiftCertificate(@ApiParam(value = "The GiftCertificate ID") @PathVariable("id") @Min(1) long id,
                                                                        @ApiParam(value = "The GiftCertificate update request dto") @RequestBody @NotNull GiftCertificateDto giftCertificate) {
        giftCertificate.setId(id);
        GiftCertificateDto updatedGiftCertificate = giftCertificateService.update(giftCertificate);
        GiftCertificateHateoas certificateHateoas = GiftCertificateHateoas.build(updatedGiftCertificate, giftCertificateHateoasProvider);
        return new ResponseEntity<>(certificateHateoas, OK);
    }

    @ApiOperation(value = "Delete GiftCertificate")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successfully deleted a GiftCertificate"),
            @ApiResponse(code = 400, message = "The GiftCertificate can't be deleted due to bad request"),
            @ApiResponse(code = 404, message = "The GiftCertificate is not found to be deleted")
    }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGiftCertificate(@ApiParam(value = "The GiftCertificate ID") @PathVariable("id") @Min(1) long id) {
        giftCertificateService.delete(id);
        return new ResponseEntity<>(NO_CONTENT);
    }
}
