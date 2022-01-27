package com.epam.esm.repository;

import com.epam.esm.entity.RefreshToken;
import com.epam.esm.entity.User;
import com.epam.esm.entity.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

import static com.epam.esm.entity.UserRoleName.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class RefreshTokenRepositoryTest {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Test
    void testFindByToken() {
        //Given
        RefreshToken expected = provideExistingRefreshToken();
        String token = expected.getToken();

        //When
        Optional<RefreshToken> actual = refreshTokenRepository.findRefreshTokenByToken(token);

        //Then
        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    void testFindByTokenNotExist() {
        //Given
        RefreshToken expected = provideNewRefreshToken();
        String token = expected.getToken();

        //When
        Optional<RefreshToken> actual = refreshTokenRepository.findRefreshTokenByToken(token);

        //Then
        assertTrue(actual.isEmpty());
    }

    @Test
    void testCreate() {
        //Given
        RefreshToken refreshToken = provideNewRefreshToken();

        //When
        RefreshToken actual = refreshTokenRepository.save(refreshToken);

        //Then
        assertNotNull(actual);
        assertTrue(actual.getId() > 0);

        //Clean
        removeRedundantRefreshToken(refreshToken);
    }



    @Test
    void testDelete() {
        //Given
        RefreshToken refreshToken = provideNewRefreshToken();
        saveNewRefreshToken(refreshToken);


        //When
        refreshTokenRepository.delete(refreshToken);

        //Then
        Optional<RefreshToken> deletedRefreshToken = refreshTokenRepository.findRefreshTokenByToken(refreshToken.getToken());
        assertFalse(deletedRefreshToken.isPresent());
    }

    private User provideUserWithToken() {
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

    private User provideUserWithoutToken() {
        User user = new User();
        user.setId(2L);
        user.setLogin("cindy_clark");
        user.setPassword("a0f3285b07c26c0dcd2191447f391170d06035e8d57e31a048ba87074f3a9a15");
        user.setRole(provideRole());
        user.setName("Cindy");
        user.setSurname("Clark");
        user.setEmail("cindy.clark@gmail.com");
        return user;
    }

    private UserRole provideRole() {
        UserRole role = new UserRole();
        role.setId(1);
        role.setName(USER);
        return role;
    }

    //Refresh token for user with id = 1
    private RefreshToken provideExistingRefreshToken() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setId(1L);
        refreshToken.setToken("6fbcf58e-d213-47e8-bc5b-764e3f4c28f6");
        refreshToken.setExpirationDate(Instant.from(ZonedDateTime.of(2032, 1, 1, 11, 11, 11, 222000000, ZoneId.of("Europe/Minsk"))));
        refreshToken.setUser(provideUserWithToken());
        return refreshToken;
    }

    //Refresh token for user with id = 2
    private RefreshToken provideNewRefreshToken() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken("11b97e8d-8b86-4d93-a575-776efa02f76e");
        refreshToken.setExpirationDate(Instant.from(ZonedDateTime.of(2032, 1, 1, 11, 11, 11, 222000000, ZoneId.of("Europe/Minsk"))));
        refreshToken.setUser(provideUserWithoutToken());
        return refreshToken;
    }

    private RefreshToken saveNewRefreshToken(RefreshToken refreshToken) {

        long generatedId = refreshTokenRepository.save(refreshToken).getId();
        assertTrue(generatedId > 0);

        return refreshToken;
    }

    private void removeRedundantRefreshToken(RefreshToken refreshToken) {
        refreshTokenRepository.delete(refreshToken);
    }
}
