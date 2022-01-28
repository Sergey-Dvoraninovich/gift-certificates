package com.epam.esm.repository.mapping;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.Duration;

@Converter
public class DurationConverter implements
        AttributeConverter<Duration, Long> {

    @Override
    public Long convertToDatabaseColumn(Duration duration) {
        if (duration == null) {
            return null;
        }

        return duration.toDays();
    }

    @Override
    public Duration convertToEntityAttribute(Long durationInDays) {
        if (durationInDays == null) {
            return null;
        }

        return Duration.ofDays(durationInDays);
    }
}
