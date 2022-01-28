package com.epam.esm.exception;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ExceptionResponse {

    @ApiModelProperty(notes = "The error Message")
    private String errorMessage;

    @ApiModelProperty(notes = "The error Code")
    private Long errorCode;
}
