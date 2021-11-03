package com.epam.esm.controller;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/giftCertificates")
public class GiftCertificateController {
    private GiftCertificateService giftCertificateService;

    public GiftCertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    @GetMapping
    public ResponseEntity<List<GiftCertificateDto>> getGiftCertificates(@PathParam("tagName") String tagName,
                                                                        @PathParam("certificateName") String certificateName,
                                                                        @PathParam("orderingName") String orderingName,
                                                                        @PathParam("certificateDescription") String certificateDescription,
                                                                        @PathParam("orderingDate") String orderingDate) {
        List<GiftCertificateDto> giftCertificatesDto = giftCertificateService.findAll(tagName, certificateName, orderingName,
                                                                                      certificateDescription, orderingDate);
        return new ResponseEntity<>(giftCertificatesDto, OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GiftCertificateDto> getGiftCertificate(@PathVariable("id") long id) {
        GiftCertificateDto giftCertificateDto = giftCertificateService.findById(id);
        return new ResponseEntity<>(giftCertificateDto, OK);
    }

    @PostMapping
    public ResponseEntity<GiftCertificateDto> createGiftCertificate(@RequestBody GiftCertificateDto giftCertificateDto) {
        GiftCertificateDto createdGiftCertificate = giftCertificateService.create(giftCertificateDto);
        return new ResponseEntity<>(createdGiftCertificate, OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<GiftCertificateDto> updateGiftCertificate(@PathVariable("id") long id,
                                                                    @RequestBody GiftCertificateDto giftCertificate) {
        System.out.println(id);
        System.out.println(giftCertificate);
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
