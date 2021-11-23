package com.epam.esm.hateos;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.controller.OrderController;
import com.epam.esm.dto.OrderItemDto;
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
public class OrderItemHateoas extends RepresentationModel<OrderItemHateoas> {
    private static final String PARTIAL_GIFT_CERTIFICATE_REL = "giftCertificate_";

    private OrderItemDto orderItemDto;

    public static OrderItemHateoas build(Long orderId, OrderItemDto orderItemDto) {
        OrderItemHateoas hateoasModel = new OrderItemHateoas(orderItemDto);

        List<Link> orderItemLinks = new ArrayList<>();
        Link selfLink = linkTo(methodOn(OrderController.class).getOrderItem(orderId, orderItemDto.getId()))
                .withSelfRel();
        orderItemLinks.add(selfLink);
        Link certificateLink = linkTo(methodOn(GiftCertificateController.class).getGiftCertificate(orderItemDto.getId()))
                .withRel(PARTIAL_GIFT_CERTIFICATE_REL + orderItemDto.getId());
        orderItemLinks.add(certificateLink);

        hateoasModel.add(orderItemLinks);
        return hateoasModel;
    }
}
