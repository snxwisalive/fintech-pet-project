package com.example.fintech.DTO;

import lombok.*;
import jakarta.validation.constraints.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserUpdateDTO {
	@Size(min = 2, max = 16, message = "The first name must be between 2 and 16 elements")
	@Pattern(regexp = ".*\\S.*", message = "The first name can't contain only spaces")
	private String firstName;

	@Size(min = 2, max = 16, message = "The last name must be between 2 and 16 elements")
	@Pattern(regexp = ".*\\S.*", message = "The last name can't contain only spaces")
	private String lastName;

	@Pattern(regexp = "^\\+?[0-9]{10,13}$", message = "Wrong phone number format")
	private String phoneNumber;
}