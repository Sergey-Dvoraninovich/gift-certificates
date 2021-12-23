package com.epam.esm.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TagDto extends RepresentationModel<TagDto> {
    @ApiModelProperty(notes = "The tag ID")
    private long id;

    @ApiModelProperty(notes = "The tag name")
    private String name;
}
