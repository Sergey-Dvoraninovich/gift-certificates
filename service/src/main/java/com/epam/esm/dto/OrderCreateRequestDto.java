package com.epam.esm.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateRequestDto {
    @ApiModelProperty(notes = "The Buyer of GiftCertificates")
    private UserDto user;
    @ApiModelProperty(notes = "The Ordered GiftCertificates")
    private List<OrderItemDto> orderGiftCertificates;
}
