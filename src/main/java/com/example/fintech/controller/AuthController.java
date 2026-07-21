package com.example.fintech.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fintech.DTO.AuthRequestDTO;
import com.example.fintech.DTO.AuthResponseDTO;
import com.example.fintech.DTO.UserCreationDTO;
import com.example.fintech.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    
    @PostMapping("/register")
    public AuthResponseDTO register(@Valid @RequestBody UserCreationDTO dto) {
        return authService.register(dto);
    }

    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody AuthRequestDTO dto) {
        return authService.login(dto);
    }
}
