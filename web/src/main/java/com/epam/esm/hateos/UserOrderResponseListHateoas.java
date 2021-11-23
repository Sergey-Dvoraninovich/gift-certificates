package com.epam.esm.hateos;

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
public class UserOrderResponseListHateoas extends RepresentationModel<UserOrderResponseListHateoas> {
    private static final String PREV_PAGE_REL = "prevPage";
    private static final String NEXT_PAGE_REL = "nextPage";

    private List<UserOrderResponseHateoas> userOrderResponsesDto;

    public static UserOrderResponseListHateoas build(long userId, List<UserOrderResponseDto> usersDto,
                                                     Long usersDtoAmount, Integer pageNumber, Integer pageSize) {
        List<UserOrderResponseHateoas> userOrderResponseListHateoas = new ArrayList<>();
        for (UserOrderResponseDto userOrderResponseDto: usersDto) {
            userOrderResponseListHateoas.add(UserOrderResponseHateoas.build(userId, userOrderResponseDto));
        }

        UserOrderResponseListHateoas hateoasListModel = new UserOrderResponseListHateoas(userOrderResponseListHateoas);

        List<Link> userLinks = new ArrayList<>();
        if (pageNumber > 1) {
            Link prevLink = linkTo(methodOn(UserController.class).getUserOrders(userId, pageNumber - 1, pageSize)).withRel(PREV_PAGE_REL);
            userLinks.add(prevLink);
        }
        Link selfLink = linkTo(methodOn(UserController.class).getUserOrders(userId, pageNumber, pageSize)).withSelfRel();
        userLinks.add(selfLink);
        if (usersDtoAmount > pageNumber * pageSize) {
            Link nextLink = linkTo(methodOn(UserController.class).getUserOrders(userId,pageNumber + 1, pageSize)).withRel(NEXT_PAGE_REL);
            userLinks.add(nextLink);
        }

        hateoasListModel.add(userLinks);
        return hateoasListModel;
    }
}