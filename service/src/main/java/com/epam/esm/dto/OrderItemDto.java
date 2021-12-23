package com.epam.esm.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderItemDto extends RepresentationModel<OrderItemDto> {
    @ApiModelProperty(notes = "The GiftCertificate ID")
    long id;

    @ApiModelProperty(notes = "The price of a GiftCertificate")
    BigDecimal price;
}
