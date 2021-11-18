package com.epam.esm.hateos.provider.impl;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.controller.TagController;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.hateos.provider.HateoasProvider;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class GiftCertificateHateoasProvider implements HateoasProvider<GiftCertificateDto> {
    @Override
    public List<Link> provide(GiftCertificateDto certificateDto) {
        List<Link> certificateLinks = new ArrayList<>();
        Link selfLink = linkTo(GiftCertificateController.class).slash(certificateDto.getId()).withSelfRel();
        certificateLinks.add(selfLink);

        for (TagDto tagDto: certificateDto.getTagsDto()) {
            Link tagLink = linkTo(TagController.class).slash(tagDto.getId()).withRel(tagDto.getName());
            certificateLinks.add(tagLink);
        }
        return certificateLinks;
    }
}
