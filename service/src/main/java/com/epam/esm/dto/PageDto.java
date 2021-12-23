package com.epam.esm.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PageDto {

    @ApiModelProperty(notes = "The page number")
    private int pageNumber;

    @ApiModelProperty(notes = "The page size")
    private int pageSize;

    public Pageable toPageable() {
        return PageRequest.of(pageNumber - 1, pageSize);
    }
}
