package com.epam.esm.service;

import com.epam.esm.entity.User;
import com.epam.esm.entity.UserRole;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.impl.UserAuthenticationProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static com.epam.esm.entity.UserRoleName.USER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserAuthenticationProviderTest {
    @InjectMocks
    private UserAuthenticationProvider userAuthenticationProvider;

    @Mock
    private UserRepository userRepository;

    @BeforeAll
    static void setUp() {
        MockitoAnnotations.openMocks(UserAuthenticationProviderTest.class);
    }

    @Test
    void testAuthenticate() {
        User user = provideUser();
        when(userRepository.findByLogin(user.getLogin())).thenReturn(Optional.of(user));
        UsernamePasswordAuthenticationToken authRequest;
        authRequest = new UsernamePasswordAuthenticationToken(user.getLogin(), user.getPassword());
        String expectedAuthorityLine = "ROLE_USER";

        Authentication authentication = userAuthenticationProvider.authenticate(authRequest);

        assertNotNull(authentication);
        assertNotNull(authentication.getPrincipal());
        assertTrue(authentication.isAuthenticated());
        assertEquals(expectedAuthorityLine, authentication.getAuthorities().toArray()[0].toString());
    }

    private User provideUser() {
        User user = new User();
        user.setId(1L);
        user.setLogin("ivan_ivanov");
        user.setRole(provideRole());
        user.setPassword("a0f3285b07c26c0dcd2191447f391170d06035e8d57e31a048ba87074f3a9a15");
        user.setName("Ivan");
        user.setSurname("Ivanov");
        user.setEmail("ivan.ivanov@gmail.com");
        return user;
    }

    private UserRole provideRole() {
        UserRole role = new UserRole();
        role.setId(1);
        role.setName(USER);
        return role;
    }
}
