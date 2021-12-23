package com.epam.esm.provider.impl;

import com.epam.esm.controller.UserOrderController;
import com.epam.esm.dto.UserOrderResponseDto;
import com.epam.esm.provider.ChildLinksProvider;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserOrderLinksProvider implements ChildLinksProvider<Long, UserOrderResponseDto> {
    private static final String USER_ORDERS_REL = "orders";

    public List<Link> provide(Long userId, UserOrderResponseDto userOrderResponseDto) {

        List<Link> orderResponseLinks = new ArrayList<>();
        Link selfLink = linkTo(methodOn(UserOrderController.class).getUserOrder(userId, userOrderResponseDto.getId()))
                .withSelfRel();
        orderResponseLinks.add(selfLink);

        return orderResponseLinks;
    }
}
