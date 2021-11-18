package com.epam.esm.hateos;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.hateos.provider.HateoasProvider;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Data
@AllArgsConstructor
public class GiftCertificateHateoas extends RepresentationModel<GiftCertificateDto> {
    private GiftCertificateDto giftCertificateDto;

    public static GiftCertificateHateoas build(GiftCertificateDto certificateDto, HateoasProvider<GiftCertificateDto> hateoasProvider) {
        GiftCertificateHateoas hateoasModel = new GiftCertificateHateoas(certificateDto);
        List<Link> tagLinks = hateoasProvider.provide(certificateDto);
        hateoasModel.add(tagLinks);
        return hateoasModel;
    }
}
