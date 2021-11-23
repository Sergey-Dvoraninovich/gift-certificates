package com.epam.esm.hateos;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.dto.GiftCertificateResponseDto;
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
public class GiftCertificateResponseListHateoas extends RepresentationModel<GiftCertificateResponseHateoas> {
    private static final String PREV_PAGE_REL = "prevPage";
    private static final String NEXT_PAGE_REL = "nextPage";

    private List<GiftCertificateResponseHateoas> giftCertificatesDto;

    public static GiftCertificateResponseListHateoas build(List<GiftCertificateResponseDto> certificatesDto, HateoasProvider<GiftCertificateResponseDto> hateoasProvider,
                                                           String[] tagNames, String certificateName, String orderingName,
                                                           String certificateDescription, String orderingDate,
                                                           Long tagsDtoAmount, Integer pageNumber, Integer pageSize) {
        List<GiftCertificateResponseHateoas> certificateListHateoas = new ArrayList<>();
        for (GiftCertificateResponseDto certificateDto: certificatesDto) {
            certificateListHateoas.add(GiftCertificateResponseHateoas.build(certificateDto, hateoasProvider));
        }

        GiftCertificateResponseListHateoas hateoasListModel = new GiftCertificateResponseListHateoas(certificateListHateoas);

        List<Link> certificateLinks = new ArrayList<>();
        if (pageNumber > 1) {
            String prevLinkLine = LinkProcessor.process(
                    linkTo(methodOn(GiftCertificateController.class)
                            .getGiftCertificates(tagNames, certificateName, orderingName, certificateDescription,
                                    orderingDate, pageNumber - 1, pageSize)
            ));
            certificateLinks.add(new Link(prevLinkLine).withRel(PREV_PAGE_REL));
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
            certificateLinks.add(new Link(nextLinkLine).withRel(NEXT_PAGE_REL));
        }

        hateoasListModel.add(certificateLinks);
        return hateoasListModel;
    }
}
