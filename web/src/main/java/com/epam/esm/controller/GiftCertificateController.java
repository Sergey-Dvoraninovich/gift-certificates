package com.epam.esm.controller;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.exception.InvalidPaginationException;
import com.epam.esm.hateos.GiftCertificateHateoas;
import com.epam.esm.hateos.GiftCertificateListHateoas;
import com.epam.esm.hateos.provider.impl.GiftCertificateHateoasProvider;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.validator.PaginationValidator;
import com.epam.esm.validator.ValidationError;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.Collections;
import java.util.List;

import static com.epam.esm.validator.ValidationError.PAGE_IS_OUT_OF_RANGE;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/giftCertificates")
@RequiredArgsConstructor
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;
    private final GiftCertificateHateoasProvider giftCertificateHateoasProvider;
    private final PaginationValidator paginationValidator;

    @GetMapping
    public ResponseEntity<GiftCertificateListHateoas> getGiftCertificates(@PathParam("tagName") String[] tagNames,
                                                                          @PathParam("certificateName") String certificateName,
                                                                          @PathParam("orderingName") String orderingName,
                                                                          @PathParam("certificateDescription") String certificateDescription,
                                                                          @PathParam("orderingDate") String orderingDate,
                                                                          @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
                                                                          @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

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

    @GetMapping("/{id}")
    public ResponseEntity<GiftCertificateHateoas> getGiftCertificate(@PathVariable("id") long id) {
        GiftCertificateDto giftCertificateDto = giftCertificateService.findById(id);
        GiftCertificateHateoas certificateHateoas = GiftCertificateHateoas.build(giftCertificateDto, giftCertificateHateoasProvider);
        return new ResponseEntity<>(certificateHateoas, OK);
    }

    @PostMapping
    public ResponseEntity<GiftCertificateHateoas> createGiftCertificate(@RequestBody GiftCertificateDto giftCertificateDto) {
        GiftCertificateDto createdGiftCertificate = giftCertificateService.create(giftCertificateDto);
        GiftCertificateHateoas certificateHateoas = GiftCertificateHateoas.build(createdGiftCertificate, giftCertificateHateoasProvider);
        return new ResponseEntity<>(certificateHateoas, CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<GiftCertificateHateoas> updateGiftCertificate(@PathVariable("id") long id,
                                                                    @RequestBody GiftCertificateDto giftCertificate) {
        giftCertificate.setId(id);
        GiftCertificateDto updatedGiftCertificate = giftCertificateService.update(giftCertificate);
        GiftCertificateHateoas certificateHateoas = GiftCertificateHateoas.build(updatedGiftCertificate, giftCertificateHateoasProvider);
        return new ResponseEntity<>(certificateHateoas, OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGiftCertificate(@PathVariable("id") long id) {
        giftCertificateService.delete(id);
        return new ResponseEntity<>(NO_CONTENT);
    }
}
