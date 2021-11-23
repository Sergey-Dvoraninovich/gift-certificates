package com.epam.esm.hateos.provider.impl;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.controller.OrderController;
import com.epam.esm.dto.OrderItemDto;
import com.epam.esm.dto.OrderResponseDto;
import com.epam.esm.hateos.provider.HateoasProvider;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderResponseHateoasProvider implements HateoasProvider<OrderResponseDto> {
    private static final String PARTIAL_GIFT_CERTIFICATE_REL = "giftCertificate_";
    private static final String PARTIAL_ORDER_ITEM_REL = "orderItem_";
    private static final String ORDER_ITEMS = "orderItems";

    @Override
    public List<Link> provide(OrderResponseDto orderResponseDto) {
        List<Link> orderLinks = new ArrayList<>();
        Link selfLink = linkTo(OrderController.class).slash(orderResponseDto.getId()).withSelfRel();
        orderLinks.add(selfLink);

        for (OrderItemDto orderItemDto: orderResponseDto.getOrderGiftCertificates()) {
            long id = orderItemDto.getId();

            Link certificateLink = linkTo(methodOn(GiftCertificateController.class).getGiftCertificate(orderItemDto.getId()))
                    .withRel(PARTIAL_GIFT_CERTIFICATE_REL + id);
            orderLinks.add(certificateLink);

            Link orderItemLink = linkTo(OrderController.class)
                    .slash(orderResponseDto.getId())
                    .slash(ORDER_ITEMS)
                    .slash(orderItemDto.getId())
                    .withRel(PARTIAL_ORDER_ITEM_REL + orderItemDto.getId());
            orderLinks.add(orderItemLink);
        }
        return orderLinks;
    }
}