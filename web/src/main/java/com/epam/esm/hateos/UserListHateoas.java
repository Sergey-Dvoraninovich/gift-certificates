package com.epam.esm.hateos;

import com.epam.esm.controller.TagController;
import com.epam.esm.controller.UserController;
import com.epam.esm.dto.UserDto;
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
public class UserListHateoas extends RepresentationModel<UserListHateoas> {
    private static final String PREV_PAGE_REL = "prevPage";
    private static final String NEXT_PAGE_REL = "nextPage";

    private List<UserHateoas> usersDto;

    public static UserListHateoas build(List<UserDto> usersDto, HateoasProvider<UserDto> hateoasProvider,
                                       Long usersDtoAmount, Integer pageNumber, Integer pageSize) {
        List<UserHateoas> userListHateoas = new ArrayList<>();
        for (UserDto userDto: usersDto) {
            userListHateoas.add(UserHateoas.build(userDto, hateoasProvider));
        }

        UserListHateoas hateoasListModel = new UserListHateoas(userListHateoas);

        List<Link> userLinks = new ArrayList<>();
        if (pageNumber > 1) {
            Link prevLink = linkTo(methodOn(UserController.class).getUsers(pageNumber - 1, pageSize)).withRel(PREV_PAGE_REL);
            userLinks.add(prevLink);
        }
        Link selfLink = linkTo(methodOn(UserController.class).getUsers(pageNumber, pageSize)).withSelfRel();
        userLinks.add(selfLink);
        if (usersDtoAmount > pageNumber * pageSize) {
            Link nextLink = linkTo(methodOn(UserController.class).getUsers(pageNumber + 1, pageSize)).withRel(NEXT_PAGE_REL);
            userLinks.add(nextLink);
        }

        hateoasListModel.add(userLinks);
        return hateoasListModel;
    }
}
