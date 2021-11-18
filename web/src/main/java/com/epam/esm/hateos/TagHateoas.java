package com.epam.esm.hateos;

import com.epam.esm.dto.TagDto;
import com.epam.esm.hateos.provider.HateoasProvider;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Data
@AllArgsConstructor
public class TagHateoas extends RepresentationModel<TagDto> {
    private TagDto tagDto;

    public static TagHateoas build(TagDto tagDto, HateoasProvider<TagDto> hateoasProvider) {
        TagHateoas hateoasModel = new TagHateoas(tagDto);
        List<Link> tagLinks = hateoasProvider.provide(tagDto);
        hateoasModel.add(tagLinks);
        return hateoasModel;
    }
}
