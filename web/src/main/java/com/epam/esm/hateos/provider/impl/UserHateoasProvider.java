package com.epam.esm.hateos.provider.impl;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.controller.UserController;
import com.epam.esm.dto.UserDto;
import com.epam.esm.hateos.provider.HateoasProvider;
import com.epam.esm.hateos.util.LinkProcessor;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserHateoasProvider implements HateoasProvider<UserDto> {
    @Override
    public List<Link> provide(UserDto userDto) {
        List<Link> userLinks = new ArrayList<>();
        Link selfLink = linkTo(UserController.class).slash(userDto.getId()).withSelfRel();
        String ordersLinkLine = LinkProcessor.process(
                linkTo(methodOn(UserController.class).getUserOrders(userDto.getId(), null, null)));
        Link ordersLink = new Link(ordersLinkLine).withRel("orders");
        userLinks.add(selfLink);
        userLinks.add(ordersLink);
        return userLinks;
        }
}
