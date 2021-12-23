package com.epam.esm.controller.impl;

import com.epam.esm.controller.AuthenticationController;
import com.epam.esm.dto.TokenDto;
import com.epam.esm.dto.UserSignInDto;
import com.epam.esm.dto.UserSignUpDto;
import com.epam.esm.service.RefreshTokenService;
import com.epam.esm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationControllerImpl implements AuthenticationController {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    @Override
    public ResponseEntity<TokenDto> signup(@RequestBody UserSignUpDto userSignUpDto) {
        TokenDto tokenDto = userService.signUp(userSignUpDto);
        return new ResponseEntity<>(tokenDto, CREATED);
    }

    @Override
    public ResponseEntity<TokenDto> login(@RequestBody UserSignInDto userSignInDto) {
        TokenDto tokenDto = userService.login(userSignInDto);
        return new ResponseEntity<>(tokenDto, OK);
    }

    @Override
    public ResponseEntity<TokenDto> refreshToken(@RequestBody TokenDto tokenDto) {
        TokenDto refreshedTokenDto = refreshTokenService.refreshToken(tokenDto.getRefreshToken());
        return new ResponseEntity<>(refreshedTokenDto, OK);
    }
}
