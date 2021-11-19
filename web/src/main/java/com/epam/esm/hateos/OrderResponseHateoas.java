package com.epam.esm.hateos;

import com.epam.esm.dto.OrderResponseDto;
import com.epam.esm.hateos.provider.HateoasProvider;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Data
@AllArgsConstructor
public class OrderResponseHateoas extends RepresentationModel<OrderResponseHateoas> {
    private OrderResponseDto orderResponseDto;

    public static OrderResponseHateoas build(OrderResponseDto orderResponseDto, HateoasProvider<OrderResponseDto> hateoasProvider) {
        OrderResponseHateoas hateoasModel = new OrderResponseHateoas(orderResponseDto);
        List<Link> orderResponseLinks = hateoasProvider.provide(orderResponseDto);
        hateoasModel.add(orderResponseLinks);
        return hateoasModel;
    }
}
