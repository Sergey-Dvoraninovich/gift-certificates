package com.epam.esm.service.impl;

import com.epam.esm.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class UserAuthenticationProvider implements AuthenticationProvider {
    private final UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String login = authentication.getName();
        String password = authentication.getCredentials().toString();

        Optional<com.epam.esm.entity.User> optionalUser = userRepository.findByLogin(login);
        if (optionalUser.isEmpty()) {
            throw new BadCredentialsException("Unknown user " + login);
        }
        com.epam.esm.entity.User user = optionalUser.get();

        if (!password.equals(user.getPassword())) {
            throw new BadCredentialsException("Bad password");
        }

        UserDetails principal = User.builder()
                .username(user.getLogin())
                .password(user.getPassword())
                .roles(user.getRole().getName().name())
                .build();
        return new UsernamePasswordAuthenticationToken(
                principal, password, principal.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
