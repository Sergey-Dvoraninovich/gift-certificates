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
public class TokenDto {
    @ApiModelProperty(notes = "The access Token")
    private String accessToken;

    @ApiModelProperty(notes = "The refresh Token")
    private String refreshToken;
}
