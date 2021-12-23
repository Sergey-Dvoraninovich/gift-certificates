package com.epam.esm.dto;

import com.epam.esm.repository.OrderingType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class GiftCertificateFilterDto {
    @ApiModelProperty(notes = "The Gift Certificate name filter param")
    private String name;

    @ApiModelProperty(notes = "The Gift Certificate name filter param")
    private String description;

    @ApiModelProperty(notes = "The Gift Certificate Tag names filter param")
    private List<String> tagNames;

    @ApiModelProperty(notes = "The Gift Certificate name ordering param")
    private OrderingType orderingName;

    @ApiModelProperty(notes = "The Gift Certificate create date ordering param")
    private OrderingType orderingCreateDate;
}
