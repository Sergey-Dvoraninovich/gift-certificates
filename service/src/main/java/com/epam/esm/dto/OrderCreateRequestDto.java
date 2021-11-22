package com.epam.esm.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateRequestDto {
    @ApiModelProperty(notes = "The Buyer of GiftCertificates")
    private long userId;
    @ApiModelProperty(notes = "The Ordered GiftCertificates")
    private List<OrderItemDto> orderGiftCertificates;
}
