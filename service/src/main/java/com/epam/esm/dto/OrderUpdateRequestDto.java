package com.epam.esm.dto;

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
public class OrderUpdateRequestDto {
    @ApiModelProperty(notes = "The ordered GiftCertificate")
    private List<OrderItemDto> orderGiftCertificates;
}
