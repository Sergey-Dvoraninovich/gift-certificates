package com.epam.esm.dto;

import com.epam.esm.dto.mapping.DurationDeserializer;
import com.epam.esm.dto.mapping.DurationSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
public class GiftCertificateDto extends RepresentationModel<GiftCertificateDto> {
    private long id;
    private String name;
    private String description;
    private BigDecimal price;

    @JsonSerialize(using = DurationSerializer.class)
    @JsonDeserialize(using = DurationDeserializer.class)
    private Duration duration;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", timezone = "Europe/Minsk")
    private Instant createDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", timezone = "Europe/Minsk")
    private Instant lastUpdateDate;

    private List<TagDto> tagsDto;

}
