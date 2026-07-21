package com.example.fintech.DTO;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AuthRequestDTO {
    private String email;
    private String password;
}