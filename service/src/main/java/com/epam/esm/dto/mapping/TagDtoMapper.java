package com.epam.esm.dto.mapping;

import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class TagDtoMapper {
    @Autowired
    private ModelMapper mapper;

    public Tag toEntity(TagDto dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, Tag.class);
    }

    public TagDto toDto(Tag entity) {
        return Objects.isNull(entity) ? null : mapper.map(entity, TagDto.class);
    }
}
