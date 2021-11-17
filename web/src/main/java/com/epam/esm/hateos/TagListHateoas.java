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
public class TagListHateoas extends RepresentationModel<TagDto> {
    private List<TagHateoas> data;

    public static TagListHateoas build(List<TagDto> tagsDto, HateoasProvider<TagDto> hateoasProvider) {
        List<TagHateoas> tagListHateoas = new ArrayList<>();
        for (TagDto tag: tagsDto) {
           tagListHateoas.add(TagHateoas.build(tag, hateoasProvider));
        }

        TagListHateoas hateoasListModel = new TagListHateoas(tagListHateoas);

        List<Link> tagLinks = new ArrayList<>();
        Link selfLink = linkTo(TagController.class).withSelfRel();
        tagLinks.add(selfLink);

        hateoasListModel.add(tagLinks);
        return hateoasListModel;
    }
}