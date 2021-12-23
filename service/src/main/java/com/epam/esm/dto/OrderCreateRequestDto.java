package com.epam.esm.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderCreateRequestDto {
    @ApiModelProperty(notes = "The Buyer of GiftCertificates")
    private long userId;
    @ApiModelProperty(notes = "The Ordered GiftCertificates")
    private List<OrderItemDto> orderGiftCertificates;
}
