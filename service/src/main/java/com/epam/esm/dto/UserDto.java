package com.epam.esm.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @ApiModelProperty(notes = "The user ID")
    private long id;

    @ApiModelProperty(notes = "The user login")
    private String login;

    @ApiModelProperty(notes = "The user's first name")
    private String name;

    @ApiModelProperty(notes = "The user's last name")
    private String surname;

    @ApiModelProperty(notes = "The user's email")
    private String email;
}
