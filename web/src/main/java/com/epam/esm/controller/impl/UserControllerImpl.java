package com.epam.esm.controller.impl;

import com.epam.esm.controller.UserController;
import com.epam.esm.dto.PageDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.provider.PageModelProvider;
import com.epam.esm.provider.impl.UserLinksProvider;
import com.epam.esm.service.RequestService;
import com.epam.esm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    private static final String ACCESS_DENIED_FOR_USER = "Access denied for user";

    private final UserService userService;
    private final RequestService requestService;
    private final UserLinksProvider userLinksProvider;
    private final PageModelProvider pageModelProvider;

    @Override
    public ResponseEntity<PagedModel<UserDto>> getAll(@RequestParam Map<String, Object> params) {

        long usersAmount = userService.countAll();
        PageDto pageDto = requestService.createPageDTO(params, usersAmount);

        List<UserDto> usersDto = userService.findAll(pageDto);
        usersDto.forEach(userDto -> userDto.add(userLinksProvider.provide(userDto)));

        PagedModel<UserDto> pagedModel = pageModelProvider.provide(this.getClass(),
                params, usersDto,
                usersAmount, pageDto);

        return new ResponseEntity<>(pagedModel, OK);
    }

    @Override
    public ResponseEntity<UserDto> getUser(@PathVariable("id") @Min(1) long id) {
        UserDto userDto = userService.findById(id);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (containsAuthority(auth.getAuthorities(), "ROLE_USER")) {
            checkUserAuthority(userDto, auth.getName());
        }

        userDto.add(userLinksProvider.provide(userDto));
        return new ResponseEntity<>(userDto, OK);
    }

    private void checkUserAuthority(UserDto requestedUserDto, String login){
        if (!requestedUserDto.getLogin().equals(login)) {
            throw new AccessDeniedException(ACCESS_DENIED_FOR_USER);
        }
    }

    private boolean containsAuthority(Collection<? extends GrantedAuthority> authorities, String providedAuthority) {
        return authorities.stream()
                .map(Object::toString)
                .anyMatch(authorityLine -> authorityLine.equals(providedAuthority));
    }
}
