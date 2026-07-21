package com.example.fintech.DTO;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AuthResponseDTO {
    private String token;
}