package com.epam.esm.dto;

import com.epam.esm.repository.OrderingType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderFilterDto {

    @ApiModelProperty(notes = "The Order create time ordering param")
    private OrderingType orderingCreateTime;
}
