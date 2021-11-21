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

@Component
public class OrderResponseHateoasProvider implements HateoasProvider<OrderResponseDto> {
    @Override
    public List<Link> provide(OrderResponseDto orderResponseDto) {
        List<Link> orderLinks = new ArrayList<>();
        Link selfLink = linkTo(OrderController.class).slash(orderResponseDto.getId()).withSelfRel();
        orderLinks.add(selfLink);

        for (OrderItemDto orderItemDto: orderResponseDto.getOrderGiftCertificates()) {
            long id = orderItemDto.getId();
            Link certificateLink = linkTo(GiftCertificateController.class).slash(id).withRel("giftCertificate_" + id);
            orderLinks.add(certificateLink);
        }
        return orderLinks;
    }
}