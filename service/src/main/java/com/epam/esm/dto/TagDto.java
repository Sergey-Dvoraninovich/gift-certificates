package com.epam.esm.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
