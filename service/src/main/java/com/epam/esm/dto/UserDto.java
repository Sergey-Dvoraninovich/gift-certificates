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
public class UserDto extends RepresentationModel<UserDto> {
    @ApiModelProperty(notes = "The user ID")
    private long id;

    @ApiModelProperty(notes = "The user login")
    private String login;

    @ApiModelProperty(notes = "The user's role")
    private String role;

    @ApiModelProperty(notes = "The user's first name")
    private String name;

    @ApiModelProperty(notes = "The user's last name")
    private String surname;

    @ApiModelProperty(notes = "The user's email")
    private String email;
}
