package com.epam.esm.controller;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.service.GiftCertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PatchMapping;

import javax.websocket.server.PathParam;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/giftCertificates")
@RequiredArgsConstructor
public class GiftCertificateController {
    private final GiftCertificateService giftCertificateService;

    @GetMapping
    public ResponseEntity<List<GiftCertificateDto>> getGiftCertificates(@PathParam("tagName") String[] tagNames,
                                                                        @PathParam("certificateName") String certificateName,
                                                                        @PathParam("orderingName") String orderingName,
                                                                        @PathParam("certificateDescription") String certificateDescription,
                                                                        @PathParam("orderingDate") String orderingDate,
                                                                        @PathParam("pageNumber") Integer pageNumber,
                                                                        @PathParam("pageSize") Integer pageSize) {
        List<GiftCertificateDto> giftCertificatesDto = giftCertificateService.findAll(tagNames, certificateName, orderingName,
                                                                                      certificateDescription, orderingDate,
                                                                                      pageNumber, pageSize);
        return new ResponseEntity<>(giftCertificatesDto, OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GiftCertificateDto> getGiftCertificate(@PathVariable("id") long id) {
        GiftCertificateDto giftCertificateDto = giftCertificateService.findById(id);
        Link selfLink = linkTo(GiftCertificateController.class).slash(giftCertificateDto.getId()).withSelfRel();
        giftCertificateDto.add(selfLink);

        List<TagDto> certificateTags = giftCertificateDto.getTagsDto();
        for (TagDto tag : certificateTags) {
            Link tagSelfLink = linkTo(TagController.class).slash(tag.getId()).withSelfRel();
            tag.add(tagSelfLink);
        }
        return new ResponseEntity<>(giftCertificateDto, OK);
    }

    @PostMapping
    public ResponseEntity<GiftCertificateDto> createGiftCertificate(@RequestBody GiftCertificateDto giftCertificateDto) {
        GiftCertificateDto createdGiftCertificate = giftCertificateService.create(giftCertificateDto);
        return new ResponseEntity<>(createdGiftCertificate, CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<GiftCertificateDto> updateGiftCertificate(@PathVariable("id") long id,
                                                                    @RequestBody GiftCertificateDto giftCertificate) {
        giftCertificate.setId(id);
        GiftCertificateDto updatedGiftCertificate = giftCertificateService.update(giftCertificate);
        return new ResponseEntity<>(updatedGiftCertificate, OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGiftCertificate(@PathVariable("id") long id) {
        giftCertificateService.delete(id);
        return new ResponseEntity<>(NO_CONTENT);
    }
}
