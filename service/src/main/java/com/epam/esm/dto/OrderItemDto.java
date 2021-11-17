package com.epam.esm.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class OrderItemDto {
    private long id;
    private BigDecimal price;
    private GiftCertificateDto giftCertificate;
}
