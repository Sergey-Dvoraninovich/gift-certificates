package com.epam.esm.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserSignUpDto {
    @ApiModelProperty(notes = "The user login")
    private String login;

    @ApiModelProperty(notes = "The user password")
    private String password;

    @ApiModelProperty(notes = "The user's first name")
    private String name;

    @ApiModelProperty(notes = "The user's last name")
    private String surname;

    @ApiModelProperty(notes = "The user's email")
    private String email;
}
