package com.epam.esm.controller.impl;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.dto.GiftCertificateFilterDto;
import com.epam.esm.dto.GiftCertificateRequestDto;
import com.epam.esm.dto.GiftCertificateResponseDto;
import com.epam.esm.dto.PageDto;
import com.epam.esm.provider.PageModelProvider;
import com.epam.esm.provider.impl.GiftCertificateLinksProvider;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.RequestService;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/giftCertificates")
@RequiredArgsConstructor
@Validated
public class GiftCertificateControllerImpl implements GiftCertificateController {

    private final GiftCertificateService giftCertificateService;
    private final GiftCertificateLinksProvider giftCertificateLinksProvider;
    private final RequestService requestService;
    private final PageModelProvider pageModelProvider;

    @Override
    public ResponseEntity<PagedModel<GiftCertificateResponseDto>> getAll(@ApiParam(value = "The Gift Certificate params") @RequestParam Map<String, Object> params) {

        GiftCertificateFilterDto filterDto = requestService.createGiftCertificateFilterDTO(params);

        long certificatesAmount = giftCertificateService.countAll(filterDto);
        PageDto pageDto = requestService.createPageDTO(params, certificatesAmount);

        List<GiftCertificateResponseDto> certificatesDto;
        certificatesDto = giftCertificateService.findAll(filterDto, pageDto);
        certificatesDto.forEach(certificateDto -> certificateDto.add(giftCertificateLinksProvider.provide(certificateDto)));

        PagedModel<GiftCertificateResponseDto> pagedModel = pageModelProvider.provide(this.getClass(),
                                                                  params, certificatesDto,
                                                                  certificatesAmount, pageDto);
        return new ResponseEntity<>(pagedModel, OK);
    }

    @Override
    public ResponseEntity<GiftCertificateResponseDto> getGiftCertificate(@ApiParam(value = "The GiftCertificate ID") @PathVariable("id") @Min(1) long id) {
        GiftCertificateResponseDto giftCertificateDto = giftCertificateService.findById(id);
        giftCertificateDto.add(giftCertificateLinksProvider.provide(giftCertificateDto));
        return new ResponseEntity<>(giftCertificateDto, OK);
    }

    @Override
    public ResponseEntity<GiftCertificateResponseDto> createGiftCertificate(@ApiParam(value = "The GiftCertificate create request dto") @RequestBody @NotNull GiftCertificateRequestDto giftCertificateDto) {
        GiftCertificateResponseDto createdGiftCertificate = giftCertificateService.create(giftCertificateDto);
        createdGiftCertificate.add(giftCertificateLinksProvider.provide(createdGiftCertificate));
        return new ResponseEntity<>(createdGiftCertificate, CREATED);
    }

    @Override
    public ResponseEntity<GiftCertificateResponseDto> updateGiftCertificate(@ApiParam(value = "The GiftCertificate ID") @PathVariable("id") @Min(1) long id,
                                                                                @ApiParam(value = "The GiftCertificate update request dto") @RequestBody @NotNull GiftCertificateRequestDto giftCertificate) {
        GiftCertificateResponseDto updatedGiftCertificate = giftCertificateService.update(id, giftCertificate);
        updatedGiftCertificate.add(giftCertificateLinksProvider.provide(updatedGiftCertificate));
        return new ResponseEntity<>(updatedGiftCertificate, OK);
    }

    @Override
    public ResponseEntity<GiftCertificateResponseDto> makeGiftCertificateAvailable(@ApiParam(value = "The GiftCertificate ID") @PathVariable("id") @Min(1) long id) {
        GiftCertificateResponseDto availableGiftCertificate = giftCertificateService.makeAvailable(id);
        availableGiftCertificate.add(giftCertificateLinksProvider.provide(availableGiftCertificate));
        return new ResponseEntity<>(availableGiftCertificate, OK);
    }

    @Override
    public ResponseEntity<Void> deleteGiftCertificate(@ApiParam(value = "The GiftCertificate ID") @PathVariable("id") @Min(1) long id) {
        giftCertificateService.disable(id);
        return new ResponseEntity<>(NO_CONTENT);
    }
}
