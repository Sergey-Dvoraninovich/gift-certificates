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
public class UserSignInDto {
    @ApiModelProperty(notes = "The user login")
    private String login;

    @ApiModelProperty(notes = "The user password")
    private String password;
}
