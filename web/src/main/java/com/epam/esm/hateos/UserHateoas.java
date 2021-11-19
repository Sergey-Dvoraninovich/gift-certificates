package com.epam.esm.hateos;

import com.epam.esm.dto.UserDto;
import com.epam.esm.hateos.provider.HateoasProvider;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Data
@AllArgsConstructor
public class UserHateoas extends RepresentationModel<UserHateoas> {
    private UserDto userDto;

    public static UserHateoas build(UserDto userDto, HateoasProvider<UserDto> hateoasProvider) {
        UserHateoas hateoasModel = new UserHateoas(userDto);
        List<Link> userLinks = hateoasProvider.provide(userDto);
        hateoasModel.add(userLinks);
        return hateoasModel;
        }
}
