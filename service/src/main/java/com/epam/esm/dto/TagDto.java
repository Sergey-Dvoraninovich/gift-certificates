package com.epam.esm.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagDto {
    @ApiModelProperty(notes = "The tag ID")
    private long id;

    @ApiModelProperty(notes = "The tag name")
    private String name;
}
