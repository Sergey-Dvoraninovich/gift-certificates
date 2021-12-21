package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserOrderResponseDto {
    @ApiModelProperty(notes = "The GiftCertificate order ID")
    private long id;

    @ApiModelProperty(notes = "The GiftCertificate count ordered")
    private int count;

    @ApiModelProperty(notes = "The total price of GiftCertificate order")
    private BigDecimal totalPrice;

    @ApiModelProperty(notes = "The time the GiftCertificate order got created")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", timezone = "Europe/Minsk")
    private Instant createOrderTime;

    @ApiModelProperty(notes = "The time the GiftCertificate order got updated")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", timezone = "Europe/Minsk")
    private Instant updateOrderTime;
}
