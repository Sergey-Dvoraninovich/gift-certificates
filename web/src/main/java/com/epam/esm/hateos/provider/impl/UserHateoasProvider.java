package com.epam.esm.hateos.provider.impl;

import com.epam.esm.controller.UserController;
import com.epam.esm.dto.UserDto;
import com.epam.esm.hateos.provider.HateoasProvider;
import org.springframework.hateoas.Link;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class UserHateoasProvider implements HateoasProvider<UserDto> {
    @Override
    public List<Link> provide(UserDto userDto) {
        List<Link> userLinks = new ArrayList<>();
        Link selfLink = linkTo(UserController.class).slash(userDto.getId()).withSelfRel();
        Link ordersLink = linkTo(methodOn(UserController.class).getUserOrders(userDto.getId())).withRel("orders");
        userLinks.add(selfLink);
        userLinks.add(ordersLink);
        return userLinks;
        }
}
