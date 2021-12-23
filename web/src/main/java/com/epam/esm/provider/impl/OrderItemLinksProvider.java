package com.epam.esm.provider.impl;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.controller.OrderController;
import com.epam.esm.dto.OrderItemDto;
import com.epam.esm.provider.ChildLinksProvider;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderItemLinksProvider implements ChildLinksProvider<Long, OrderItemDto> {
    private static final String PARTIAL_GIFT_CERTIFICATE_REL = "giftCertificate_";

    @Override
    public List<Link> provide(Long orderId, OrderItemDto orderItemDto) {
        List<Link> orderItemLinks = new ArrayList<>();

        Link selfLink = linkTo(methodOn(OrderController.class).getOrderItem(orderId, orderItemDto.getId()))
                .withSelfRel();
        orderItemLinks.add(selfLink);
        Link certificateLink = linkTo(methodOn(GiftCertificateController.class).getGiftCertificate(orderItemDto.getId()))
                .withRel(PARTIAL_GIFT_CERTIFICATE_REL + orderItemDto.getId());
        orderItemLinks.add(certificateLink);

        return orderItemLinks;
    }
}
