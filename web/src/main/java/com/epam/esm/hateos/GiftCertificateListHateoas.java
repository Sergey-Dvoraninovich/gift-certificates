package com.epam.esm.hateos;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.hateos.provider.HateoasProvider;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Data
@AllArgsConstructor
public class GiftCertificateListHateoas extends RepresentationModel<TagDto> {
    private List<GiftCertificateHateoas> data;

    public static GiftCertificateListHateoas build(List<GiftCertificateDto> certificatesDto, HateoasProvider<GiftCertificateDto> hateoasProvider,
                                       String[] tagNames, String certificateName, String orderingName,
                                       String certificateDescription, String orderingDate,
                                       Long tagsDtoAmount, Integer pageNumber, Integer pageSize) {
        List<GiftCertificateHateoas> certificateListHateoas = new ArrayList<>();
        for (GiftCertificateDto certificateDto: certificatesDto) {
            certificateListHateoas.add(GiftCertificateHateoas.build(certificateDto, hateoasProvider));
        }

        GiftCertificateListHateoas hateoasListModel = new GiftCertificateListHateoas(certificateListHateoas);

        List<Link> certificateLinks = new ArrayList<>();
        if (pageNumber > 1) {
        Link prevLink = linkTo(methodOn(GiftCertificateController.class)
                .getGiftCertificates(tagNames, certificateName, orderingName, certificateDescription, orderingDate,
                        pageNumber - 1, pageSize))
                .withRel("prevPage");
            certificateLinks.add(prevLink);
        }

        Link selfLink = linkTo(methodOn(GiftCertificateController.class)
                .getGiftCertificates(tagNames, certificateName, orderingName, certificateDescription, orderingDate,
                        pageNumber, pageSize))
                .withSelfRel();
        certificateLinks.add(selfLink);

        if (tagsDtoAmount > pageNumber * pageSize) {
        Link nextLink = linkTo(methodOn(GiftCertificateController.class)
                .getGiftCertificates(tagNames, certificateName, orderingName, certificateDescription, orderingDate,
                        pageNumber + 1, pageSize))
                .withRel("nextPage");
            certificateLinks.add(nextLink);
        }

        hateoasListModel.add(certificateLinks);
        return hateoasListModel;
    }
}
