package com.epam.esm.hateos;

import com.epam.esm.controller.OrderController;
import com.epam.esm.controller.TagController;
import com.epam.esm.dto.OrderResponseDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.hateos.provider.HateoasProvider;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Data
@AllArgsConstructor
public class OrderResponseListHateoas extends RepresentationModel<OrderResponseListHateoas> {
    private List<OrderResponseHateoas> orderResponsesDto;

    public static OrderResponseListHateoas build(List<OrderResponseDto> orderResponsesDto, HateoasProvider<OrderResponseDto> hateoasProvider,
                                                 Long ordersDtoAmount, Integer pageNumber, Integer pageSize, String sortOrder) {
        List<OrderResponseHateoas> orderListHateoas = new ArrayList<>();
        for (OrderResponseDto orderResponseDto: orderResponsesDto) {
            orderListHateoas.add(OrderResponseHateoas.build(orderResponseDto, hateoasProvider));
        }

        OrderResponseListHateoas hateoasListModel = new OrderResponseListHateoas(orderListHateoas);

        List<Link> orderLinks = new ArrayList<>();
        if (pageNumber > 1) {
            Link prevLink = linkTo(methodOn(OrderController.class).getOrders(pageNumber - 1, pageSize, sortOrder)).withRel("prevPage");
            orderLinks.add(prevLink);
        }
        Link selfLink = linkTo(methodOn(OrderController.class).getOrders(pageNumber, pageSize, sortOrder)).withSelfRel();
        orderLinks.add(selfLink);
        if (ordersDtoAmount > pageNumber * pageSize) {
            Link nextLink = linkTo(methodOn(OrderController.class).getOrders(pageNumber + 1, pageSize, sortOrder)).withRel("nextPage");
            orderLinks.add(nextLink);
        }

        hateoasListModel.add(orderLinks);
        return hateoasListModel;
    }
}
