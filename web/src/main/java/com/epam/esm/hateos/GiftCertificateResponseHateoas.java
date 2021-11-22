package com.epam.esm.hateos;

import com.epam.esm.dto.GiftCertificateResponseDto;
import com.epam.esm.hateos.provider.HateoasProvider;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Data
@AllArgsConstructor
public class GiftCertificateResponseHateoas extends RepresentationModel<GiftCertificateResponseHateoas> {
    private GiftCertificateResponseDto giftCertificateResponseDto;

    public static GiftCertificateResponseHateoas build(GiftCertificateResponseDto certificateDto, HateoasProvider<GiftCertificateResponseDto> hateoasProvider) {
        GiftCertificateResponseHateoas hateoasModel = new GiftCertificateResponseHateoas(certificateDto);
        List<Link> tagLinks = hateoasProvider.provide(certificateDto);
        hateoasModel.add(tagLinks);
        return hateoasModel;
    }
}
