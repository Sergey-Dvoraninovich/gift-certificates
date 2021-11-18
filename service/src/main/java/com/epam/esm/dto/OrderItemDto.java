package com.epam.esm.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

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
