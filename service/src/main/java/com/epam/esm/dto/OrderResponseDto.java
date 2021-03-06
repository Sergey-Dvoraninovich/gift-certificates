package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderResponseDto extends RepresentationModel<TagDto> {
    @ApiModelProperty(notes = "The order ID")
    private long id;

    @ApiModelProperty(notes = "The time the order got created")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", timezone = "Europe/Minsk")
    private Instant createOrderTime;

    @ApiModelProperty(notes = "The time of the last order update")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", timezone = "Europe/Minsk")
    private Instant updateOrderTime;

    @ApiModelProperty(notes = "The GiftCertificate count")
    private int count;

    @ApiModelProperty(notes = "The price for ")
    private BigDecimal totalPrice;

    @ApiModelProperty(notes = "The GiftCertificate Buyer")
    private UserDto user;

    @ApiModelProperty(notes = "The ordered GiftCertificate")
    private List<OrderItemDto> orderGiftCertificates;
}
