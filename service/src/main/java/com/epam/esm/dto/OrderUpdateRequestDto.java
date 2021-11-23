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
public class OrderUpdateRequestDto {
    @ApiModelProperty(notes = "The ordered GiftCertificate")
    private List<OrderItemDto> orderGiftCertificates;
}
