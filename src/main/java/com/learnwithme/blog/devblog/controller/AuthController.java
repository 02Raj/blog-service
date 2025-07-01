package com.learnwithme.blog.devblog.controller;

import com.learnwithme.blog.devblog.dto.ApiResponseDto;
import com.learnwithme.blog.devblog.dto.AuthRequestDto;
import com.learnwithme.blog.devblog.dto.AuthResponseDto;
import com.learnwithme.blog.devblog.dto.UserDto;
import com.learnwithme.blog.devblog.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * Login user
     * @param loginRequest login credentials
     * @return JWT token
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto<AuthResponseDto>> login(@RequestBody AuthRequestDto loginRequest) {
        AuthResponseDto loginResponse = authService.login(loginRequest);

        ApiResponseDto<AuthResponseDto> response = ApiResponseDto.success(
                loginResponse,
                "Login successful",
                HttpStatus.OK.value()
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Register new user
     * @param registerDto user registration data
     * @return created user
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponseDto<UserDto>> register(@Valid @RequestBody UserDto registerDto) {
        UserDto createdUser = authService.register(registerDto);

        ApiResponseDto<UserDto> response = ApiResponseDto.success(
                createdUser,
                "User registered successfully",
                HttpStatus.CREATED.value()
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}