package com.learnwithme.blog.devblog.service;


import com.learnwithme.blog.devblog.dto.AuthRequestDto;
import com.learnwithme.blog.devblog.dto.AuthResponseDto;
import com.learnwithme.blog.devblog.dto.UserDto;

public interface AuthService {

    /**
     * Login user and get JWT token
     * @param loginRequest the login credentials
     * @return authentication response with JWT token
     */
    AuthResponseDto login(AuthRequestDto loginRequest);

    /**
     * Register new user
     * @param registerDto the registration data
     * @return the created user
     */
    UserDto register(UserDto registerDto);

    /**
     * Verify if token is valid
     * @param token the JWT token
     * @return true if token is valid
     */
    boolean validateToken(String token);
}