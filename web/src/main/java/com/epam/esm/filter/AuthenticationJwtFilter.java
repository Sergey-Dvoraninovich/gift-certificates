package com.epam.esm.filter;

import com.epam.esm.dto.UserDto;
import com.epam.esm.exception.JwtTokenException;
import com.epam.esm.service.JwtService;
import com.epam.esm.service.UserService;
import com.epam.esm.validator.JwtTokenValidationError;
import com.epam.esm.validator.JwtTokenValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class AuthenticationJwtFilter extends OncePerRequestFilter {

    private static final String ACCESS_TOKEN_HEADER = "Access-Token";

    @Autowired
    private JwtService jwtService;

    @Autowired
    private JwtTokenValidator jwtTokenValidator;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String jwt = request.getHeader(ACCESS_TOKEN_HEADER);

        if (jwt != null) {
            Optional<JwtTokenValidationError> validationError = jwtTokenValidator.validateJwtToken(jwt);
            if (validationError.isPresent()) {
                throw new JwtTokenException(validationError.get());
            }

            String login = jwtService.getLoginFromJwtToken(jwt);
            setAuthentication(request, login);
        }

        filterChain.doFilter(request, response);
    }

    private void setAuthentication(HttpServletRequest request, String login) {
        UserDto userDto = userService.findByLogin(login);
        String password = userService.getUserPassword(userDto);

        UserDetails principal = User.builder()
                .username(userDto.getLogin())
                .password(password)
                .roles(userDto.getRole())
                .build();
        UsernamePasswordAuthenticationToken authentication;
        authentication = new UsernamePasswordAuthenticationToken(principal, password, principal.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
