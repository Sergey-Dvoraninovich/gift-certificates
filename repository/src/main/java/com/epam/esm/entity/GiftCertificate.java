package com.epam.esm.entity;

import com.epam.esm.repository.mapping.DurationConverter;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "gift_certificates")
public class GiftCertificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "price", nullable = false, columnDefinition="Decimal(5,2)")
    private BigDecimal price;

    @Column(name = "duration", nullable = false)
    @Convert(converter = DurationConverter.class)
    private Duration duration;

    @Column(name = "create_date", nullable = false)
    private Instant createDate;

    @Column(name = "last_update_date", nullable = false)
    private Instant lastUpdateDate;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "gift_certificates_tags",
            joinColumns = @JoinColumn(name = "id_gift_certificate"),
            inverseJoinColumns = @JoinColumn(name = "id_tag"))
    private List<Tag> giftCertificateTags = new ArrayList<>();
}
