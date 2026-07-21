package com.example.fintech.model;

import lombok.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@NotNull
	@Column
	private String firstName;

	@NotNull
	@Column
	private String lastName;

	@NotNull
	@Column
	private String password;

	@NotNull
	@Column
	private String phoneNumber;

	@NotNull
	@Column(unique = true)
	private String email;

	@Enumerated(EnumType.STRING)
	@Column
	private Role role;
}