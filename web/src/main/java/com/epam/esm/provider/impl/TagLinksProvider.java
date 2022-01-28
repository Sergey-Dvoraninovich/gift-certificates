package com.epam.esm.provider.impl;

import com.epam.esm.controller.TagController;
import com.epam.esm.dto.TagDto;
import com.epam.esm.provider.LinksProvider;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TagLinksProvider implements LinksProvider<TagDto> {
    @Override
    public List<Link> provide(TagDto tagDto) {
        List<Link> tagLinks = new ArrayList<>();
        Link selfLink = linkTo(methodOn(TagController.class).getTag(tagDto.getId())).withSelfRel();
        tagLinks.add(selfLink);
        return tagLinks;
    }
}
