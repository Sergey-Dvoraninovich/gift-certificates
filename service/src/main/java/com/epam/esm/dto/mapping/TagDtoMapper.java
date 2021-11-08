package com.epam.esm.dto.mapping;

import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class TagDtoMapper {
    private final ModelMapper mapper;

    public Tag toEntity(TagDto dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, Tag.class);
    }

    public TagDto toDto(Tag entity) {
        return Objects.isNull(entity) ? null : mapper.map(entity, TagDto.class);
    }
}
