package com.example.fintech.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.fintech.DTO.AuthRequestDTO;
import com.example.fintech.DTO.AuthResponseDTO;
import com.example.fintech.DTO.UserCreationDTO;
import com.example.fintech.mapper.UserMapper;
import com.example.fintech.model.Role;
import com.example.fintech.model.User;
import com.example.fintech.repository.UserRepository;
import com.example.fintech.security.JwtService;
import com.example.fintech.security.UserPrincipal;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthService(
        UserRepository userRepository, 
        UserMapper userMapper,
        JwtService jwtService,
        PasswordEncoder passwordEncoder,
        AuthenticationManager authenticationManager
    ) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponseDTO register(UserCreationDTO dto) {
        User user = userMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);

        String token = jwtService.generateToken(new UserPrincipal(user));
        return new AuthResponseDTO(token);
    }

    public AuthResponseDTO login(AuthRequestDTO dto) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );

        User user = userRepository.findByEmail(dto.getEmail())
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String token = jwtService.generateToken(new UserPrincipal(user));
        return new AuthResponseDTO(token);
    }
}