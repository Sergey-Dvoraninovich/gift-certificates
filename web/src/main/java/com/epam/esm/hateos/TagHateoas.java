package com.epam.esm.hateos;

import com.epam.esm.controller.TagController;
import com.epam.esm.dto.TagDto;
import com.epam.esm.hateos.provider.HateoasProvider;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Data
@AllArgsConstructor
public class TagHateoas extends RepresentationModel<TagDto> {
    private TagDto tagDto;

    public static TagHateoas build(TagDto tagDto, HateoasProvider<TagDto> hateoasProvider) {
        TagHateoas hateoasModel = new TagHateoas(tagDto);

        List<Link> tagLinks = new ArrayList<>();
        Link selfLink = linkTo(TagController.class).slash(tagDto.getId()).withSelfRel();
        tagLinks.add(selfLink);

        hateoasModel.add(tagLinks);
        return hateoasModel;
    }
}
