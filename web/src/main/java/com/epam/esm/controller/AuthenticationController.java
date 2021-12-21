package com.epam.esm.controller;

import com.epam.esm.dto.TokenDto;
import com.epam.esm.dto.UserSignInDto;
import com.epam.esm.dto.UserSignUpDto;
import com.epam.esm.service.RefreshTokenService;
import com.epam.esm.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    @ApiOperation(value = "Sign up user", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User successfully signed up"),
            @ApiResponse(code = 400, message = "The user can't be signed up due to bad request"),
    }
    )
    @PostMapping("/signup")
    public ResponseEntity<TokenDto> signup(@RequestBody UserSignUpDto userSignUpDto) {
        TokenDto tokenDto = userService.signUp(userSignUpDto);
        return new ResponseEntity<>(tokenDto, CREATED);
    }

    @ApiOperation(value = "Sign up sign in", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User successfully signed in"),
            @ApiResponse(code = 400, message = "The user can't be signed in due to bad request"),
    }
    )
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody UserSignInDto userSignInDto) {
        TokenDto tokenDto = userService.login(userSignInDto);
        return new ResponseEntity<>(tokenDto, OK);
    }

    @ApiOperation(value = "Get new TokenDto by Refresh Token", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved TokenDto"),
            @ApiResponse(code = 400, message = "The user TokenDto can't be fetched due to bad request"),
            @ApiResponse(code = 404, message = "The user Refresh Token is not found")
    }
    )
    @PostMapping("/refreshToken")
    public ResponseEntity<TokenDto> refreshToken(@RequestBody TokenDto tokenDto) {
        TokenDto refreshedTokenDto = refreshTokenService.refreshToken(tokenDto.getRefreshToken());
        return new ResponseEntity<>(refreshedTokenDto, OK);
    }
}
