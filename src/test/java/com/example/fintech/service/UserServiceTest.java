package com.example.fintech.service;

import com.example.fintech.DTO.UserCreationDTO;
import com.example.fintech.DTO.UserDTO;
import com.example.fintech.mapper.UserMapper;
import com.example.fintech.model.User;
import com.example.fintech.repository.CardRepository;
import com.example.fintech.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CardRepository cardRepository;

    @Mock
    private UserMapper userMapper;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, cardRepository, userMapper);
    }

    @Test
    void createUser_shouldReturnMappedUser() {
        UserCreationDTO request = UserCreationDTO.builder()
                .firstName("Max")
                .lastName("Verstappen")
                .email("max.verstappen@example.com")
                .phoneNumber("1234567890")
                .password("12345678")
                .build();

        User user = User.builder()
                .firstName("Max")
                .lastName("Verstappen")
                .email("max.verstappen@example.com")
                .phoneNumber("1234567890")
                .build();

        UserDTO expected = UserDTO.builder()
                .firstName("Max")
                .lastName("Verstappen")
                .phoneNumber("1234567890")
                .build();

        when(userMapper.toEntity(request)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(expected);

        UserDTO result = userService.createUser(request);

        assertEquals(expected, result);
        verify(userRepository).save(user);
        verify(userMapper).toDto(user);
    }
}
