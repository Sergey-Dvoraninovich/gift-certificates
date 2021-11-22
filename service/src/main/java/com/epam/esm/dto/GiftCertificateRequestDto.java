package com.epam.esm.dto;

import com.epam.esm.dto.mapping.DurationDeserializer;
import com.epam.esm.dto.mapping.DurationSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GiftCertificateRequestDto {
    @ApiModelProperty(notes = "The GiftCertificate name")
    private String name;

    @ApiModelProperty(notes = "The GiftCertificate description")
    private String description;

    @ApiModelProperty(notes = "The GiftCertificate price")
    private BigDecimal price;

    @JsonSerialize(using = DurationSerializer.class)
    @JsonDeserialize(using = DurationDeserializer.class)
    @ApiModelProperty(notes = "The GiftCertificate duration")
    private Duration duration;

    @ApiModelProperty(notes = "The Ids of GiftCertificate tags")
    private List<Long> tagIdsDto;
}
