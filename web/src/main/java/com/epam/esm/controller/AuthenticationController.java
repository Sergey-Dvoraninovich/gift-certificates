package com.epam.esm.controller;

import com.epam.esm.dto.TokenDto;
import com.epam.esm.dto.UserSignInDto;
import com.epam.esm.dto.UserSignUpDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/auth")
public interface AuthenticationController {

    @ApiOperation(value = "Sign up user", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User successfully signed up"),
            @ApiResponse(code = 400, message = "The user can't be signed up due to bad request"),
    }
    )
    @PostMapping("/signup")
    ResponseEntity<TokenDto> signup(@ApiParam(value = "The Sign Up Dto") @RequestBody UserSignUpDto userSignUpDto);

    @ApiOperation(value = "Sign up sign in", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User successfully signed in"),
            @ApiResponse(code = 400, message = "The user can't be signed in due to bad request"),
    }
    )
    @PostMapping("/login")
    ResponseEntity<TokenDto> login(@ApiParam(value = "The Sign In Dto") @RequestBody UserSignInDto userSignInDto);

    @ApiOperation(value = "Get new TokenDto by Refresh Token", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved TokenDto"),
            @ApiResponse(code = 400, message = "The user TokenDto can't be fetched due to bad request"),
            @ApiResponse(code = 404, message = "The user Refresh Token is not found")
    }
    )
    @PostMapping("/refreshToken")
    ResponseEntity<TokenDto> refreshToken(@ApiParam(value = "The Token Dto with refresh token") @RequestBody TokenDto tokenDto);
}
