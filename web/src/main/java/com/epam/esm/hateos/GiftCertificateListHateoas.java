package com.epam.esm.hateos;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.hateos.provider.HateoasProvider;
import com.epam.esm.hateos.util.LinkProcessor;
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
public class GiftCertificateListHateoas extends RepresentationModel<GiftCertificateHateoas> {
    private List<GiftCertificateHateoas> giftCertificatesDto;

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
            String prevLinkLine = LinkProcessor.process(
                    linkTo(methodOn(GiftCertificateController.class)
                            .getGiftCertificates(tagNames, certificateName, orderingName, certificateDescription,
                                    orderingDate, pageNumber - 1, pageSize)
            ));
            certificateLinks.add(new Link(prevLinkLine).withRel("prevPage"));
        }

        String selfLinkLine = LinkProcessor.process(
                linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificates(tagNames, certificateName, orderingName, certificateDescription,
                                orderingDate, pageNumber, pageSize)
                ));
        certificateLinks.add(new Link(selfLinkLine).withSelfRel());

        if (tagsDtoAmount > pageNumber * pageSize) {
            String nextLinkLine = LinkProcessor.process(
                    linkTo(methodOn(GiftCertificateController.class)
                            .getGiftCertificates(tagNames, certificateName, orderingName, certificateDescription,
                                    orderingDate, pageNumber + 1, pageSize)
                    ));
            certificateLinks.add(new Link(nextLinkLine).withRel("nextPage"));
        }

        hateoasListModel.add(certificateLinks);
        return hateoasListModel;
    }
}
