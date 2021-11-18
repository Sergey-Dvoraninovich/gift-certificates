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
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Data
@AllArgsConstructor
public class TagListHateoas extends RepresentationModel<TagDto> {
    private List<TagHateoas> tagsDto;

    public static TagListHateoas build(List<TagDto> tagsDto, HateoasProvider<TagDto> hateoasProvider,
                                       Long tagsDtoAmount, Integer pageNumber, Integer pageSize) {
        List<TagHateoas> tagListHateoas = new ArrayList<>();
        for (TagDto tag: tagsDto) {
           tagListHateoas.add(TagHateoas.build(tag, hateoasProvider));
        }

        TagListHateoas hateoasListModel = new TagListHateoas(tagListHateoas);

        List<Link> tagLinks = new ArrayList<>();
        if (pageNumber > 1) {
            Link prevLink = linkTo(methodOn(TagController.class).getTags(pageNumber - 1, pageSize)).withRel("prevPage");
            tagLinks.add(prevLink);
        }
        Link selfLink = linkTo(methodOn(TagController.class).getTags(pageNumber, pageSize)).withSelfRel();
        tagLinks.add(selfLink);
        if (tagsDtoAmount > pageNumber * pageSize) {
            Link nextLink = linkTo(methodOn(TagController.class).getTags(pageNumber + 1, pageSize)).withRel("nextPage");
            tagLinks.add(nextLink);
        }

        hateoasListModel.add(tagLinks);
        return hateoasListModel;
    }
}