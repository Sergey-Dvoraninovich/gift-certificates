package com.epam.esm.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {
    @ApiModelProperty(notes = "The GiftCertificate ID")
    long id;

    @ApiModelProperty(notes = "The price of a GiftCertificate")
    BigDecimal price;
}
