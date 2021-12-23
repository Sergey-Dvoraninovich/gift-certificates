package com.epam.esm.provider.impl;

import com.epam.esm.controller.UserController;
import com.epam.esm.controller.UserOrderController;
import com.epam.esm.dto.UserDto;
import com.epam.esm.provider.LinksProvider;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserLinksProvider implements LinksProvider<UserDto> {
    private static final String USER_ORDERS_REL = "orders";

    @Override
    public List<Link> provide(UserDto userDto) {
        List<Link> userLinks = new ArrayList<>();
        Link selfLink = linkTo(methodOn(UserController.class).getUser(userDto.getId())).withSelfRel();
        Link ordersLink = linkTo(methodOn(UserOrderController.class)
                .getAllItems(userDto.getId(), new HashMap<>()))
                .withRel(USER_ORDERS_REL);
        userLinks.add(selfLink);
        userLinks.add(ordersLink);
        return userLinks;
    }
}
