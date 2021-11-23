package com.epam.esm.hateos;

import com.epam.esm.controller.OrderController;
import com.epam.esm.controller.UserController;
import com.epam.esm.dto.UserOrderResponseDto;
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
public class UserOrderResponseHateoas extends RepresentationModel<UserOrderResponseHateoas> {
    private UserOrderResponseDto userOrderResponseDto;

    public static UserOrderResponseHateoas build(long userId, UserOrderResponseDto userOrderResponseDto) {
        UserOrderResponseHateoas hateoasModel = new UserOrderResponseHateoas(userOrderResponseDto);

        List<Link> orderResponseLinks = new ArrayList<>();
        Link selfLink = linkTo(methodOn(UserController.class).getUserOrder(userId, userOrderResponseDto.getId()))
                .withSelfRel();
        orderResponseLinks.add(selfLink);

        hateoasModel.add(orderResponseLinks);
        return hateoasModel;
    }
}
