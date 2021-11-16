package com.epam.esm.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@NoArgsConstructor
public class TagDto extends RepresentationModel<TagDto> {
    private long id;
    private String name;
}
