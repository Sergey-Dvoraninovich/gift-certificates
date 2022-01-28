package com.epam.esm.service;

import com.epam.esm.entity.User;
import com.epam.esm.entity.UserRole;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.repository.RefreshTokenRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.impl.RefreshTokenServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.epam.esm.entity.UserRoleName.USER;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RefreshTokenServiceTest {

    @InjectMocks
    private RefreshTokenServiceImpl refreshTokenService;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @Mock
    private UserRepository userRepository;

    @BeforeAll
    static void setUp() {
        MockitoAnnotations.openMocks(RefreshTokenService.class);
    }

    @Test
    void testSignUpUserExists() {
        User user = provideUser();

        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        try {
            refreshTokenService.createRefreshToken(user.getId());
            fail();
        } catch (EntityNotFoundException e) {
            assertTrue(true);
        }

    }

    private User provideUser() {
        User user = new User();
        user.setId(1L);
        user.setLogin("christian_altman");
        user.setPassword("a0f3285b07c26c0dcd2191447f391170d06035e8d57e31a048ba87074f3a9a15");
        user.setRole(provideRole());
        user.setName("Christian");
        user.setSurname("Altman");
        user.setEmail("christian.altman@gmail.com");
        return user;
    }

    private UserRole provideRole() {
        UserRole role = new UserRole();
        role.setId(1);
        role.setName(USER);
        return role;
    }

}
