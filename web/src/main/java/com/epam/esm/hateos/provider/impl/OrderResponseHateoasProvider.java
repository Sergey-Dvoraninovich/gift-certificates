package com.epam.esm.hateos.provider.impl;

import com.epam.esm.controller.OrderController;
import com.epam.esm.controller.TagController;
import com.epam.esm.dto.OrderResponseDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.hateos.provider.HateoasProvider;
import org.springframework.hateoas.Link;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class OrderResponseHateoasProvider implements HateoasProvider<OrderResponseDto> {
    @Override
    public List<Link> provide(OrderResponseDto orderResponseDto) {
        List<Link> tagLinks = new ArrayList<>();
        Link selfLink = linkTo(OrderController.class).slash(orderResponseDto.getId()).withSelfRel();
        tagLinks.add(selfLink);
        return tagLinks;
    }
}