package com.epam.esm.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderUpdateRequestDto {
    @ApiModelProperty(notes = "The ordered GiftCertificate")
    private List<OrderItemDto> orderGiftCertificates;
}
