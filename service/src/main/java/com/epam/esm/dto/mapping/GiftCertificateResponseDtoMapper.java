package com.epam.esm.dto.mapping;

import com.epam.esm.dto.GiftCertificateResponseDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.GiftCertificate;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GiftCertificateResponseDtoMapper {
    private final ModelMapper mapper;
    private final TagDtoMapper tagDtoMapper;

    public GiftCertificate toEntity(GiftCertificateResponseDto dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, GiftCertificate.class);
    }

    public GiftCertificateResponseDto toDto(GiftCertificate entity) {
        GiftCertificateResponseDto dto = Objects.isNull(entity) ? null : mapper.map(entity, GiftCertificateResponseDto.class);
        List<TagDto> tagsDto = entity.getGiftCertificateTags().stream()
                .map(tagDtoMapper::toDto)
                .collect(Collectors.toList());
        dto.setTagsDto(tagsDto);
        return dto;
    }
}
