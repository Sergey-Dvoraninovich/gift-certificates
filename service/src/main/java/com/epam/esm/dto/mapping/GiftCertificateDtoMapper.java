package com.epam.esm.dto.mapping;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GiftCertificateDtoMapper {
    private final ModelMapper mapper;
    private final TagDtoMapper tagDtoMapper;

    public GiftCertificate toEntity(GiftCertificateDto dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, GiftCertificate.class);
    }

    public GiftCertificateDto toDto(GiftCertificate entity) {
        GiftCertificateDto dto = Objects.isNull(entity) ? null : mapper.map(entity, GiftCertificateDto.class);
        List<TagDto> tagsDto = entity.getGiftCertificateTags().stream()
                .map(tagDtoMapper::toDto)
                .collect(Collectors.toList());
        dto.setTagsDto(tagsDto);
        return dto;
    }

}
