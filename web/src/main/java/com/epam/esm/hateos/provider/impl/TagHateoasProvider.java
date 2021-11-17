package com.epam.esm.hateos.provider.impl;

import com.epam.esm.controller.TagController;
import com.epam.esm.dto.TagDto;
import com.epam.esm.hateos.provider.HateoasProvider;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class TagHateoasProvider implements HateoasProvider<TagDto> {
    @Override
    public List<Link> provide(TagDto tagDto) {
        List<Link> tagLinks = new ArrayList<>();
        Link selfLink = linkTo(TagController.class).slash(tagDto.getId()).withSelfRel();
        tagLinks.add(selfLink);
        return tagLinks;
    }
}
