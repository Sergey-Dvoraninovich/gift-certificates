package com.epam.esm.dto.mapping;

import com.epam.esm.dto.GiftCertificateRequestDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class GiftCertificateRequestDtoMapper {
    private final ModelMapper mapper;

    public GiftCertificate toEntity(GiftCertificateRequestDto dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, GiftCertificate.class);
    }

    public GiftCertificateRequestDto toDto(GiftCertificate entity) {
        return Objects.isNull(entity) ? null : mapper.map(entity, GiftCertificateRequestDto.class);
    }
}
