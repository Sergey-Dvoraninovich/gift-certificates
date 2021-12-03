package com.epam.esm.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "price", nullable = false, columnDefinition="Decimal(5,2)")
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "id_gift_certificate")
    private GiftCertificate giftCertificate;
}
