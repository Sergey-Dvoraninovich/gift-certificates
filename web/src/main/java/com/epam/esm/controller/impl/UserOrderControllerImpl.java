package com.epam.esm.controller.impl;

import com.epam.esm.controller.UserOrderController;
import com.epam.esm.dto.PageDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.UserOrderResponseDto;
import com.epam.esm.provider.PageModelProvider;
import com.epam.esm.provider.impl.UserOrderLinksProvider;
import com.epam.esm.service.RequestService;
import com.epam.esm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/api/v1/users/{userId}/orders")
@RequiredArgsConstructor
public class UserOrderControllerImpl implements UserOrderController {

    private static final String ACCESS_DENIED_FOR_USER = "Access denied for user";

    private final UserService userService;
    private final RequestService requestService;
    private final UserOrderLinksProvider userOrderLinksProvider;
    private final PageModelProvider pageModelProvider;

    @Override
    @GetMapping()
    public ResponseEntity<PagedModel<UserOrderResponseDto>> getAllItems(@PathVariable("userId") @Min(1) long id,
                                                                        @RequestParam Map<String, Object> params) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (containsAuthority(auth.getAuthorities(), "ROLE_USER")) {
            checkUserAuthority(id, auth.getName());
        }

        long userOrdersAmount = userService.countAllUserOrders(id);
        PageDto pageDto = requestService.createPageDTO(params, userOrdersAmount);

        List<UserOrderResponseDto> userOrdersDto = userService.findUserOrders(id, pageDto);
        userOrdersDto.forEach(userOrderDto -> userOrderDto.add(userOrderLinksProvider.provide(id, userOrderDto)));

        PagedModel<UserOrderResponseDto> pagedModel = pageModelProvider.provide(this.getClass(), id,
                params, userOrdersDto,
                userOrdersAmount, pageDto);
        return new ResponseEntity<>(pagedModel, OK);
    }

    @GetMapping("/{userId}/orders/{orderId}")
    public ResponseEntity<UserOrderResponseDto> getUserOrder(@PathVariable("userId") @Min(1) long userId,
                                                             @PathVariable("orderId") @Min(1) long orderId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (containsAuthority(auth.getAuthorities(), "ROLE_USER")) {
            checkUserAuthority(userId, auth.getName());
        }

        UserOrderResponseDto userOrderDto = userService.findUserOrder(userId, orderId);
        userOrderDto.add(userOrderLinksProvider.provide(userId, userOrderDto));
        return new ResponseEntity<>(userOrderDto, OK);
    }

    private void checkUserAuthority(long requestedUserId, String login) {
        UserDto userDto = userService.findById(requestedUserId);
        checkUserAuthority(userDto, login);
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
