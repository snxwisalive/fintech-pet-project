package com.example.fintech.controller;

import com.example.fintech.service.UserService;
import com.example.fintech.service.CardService;
import com.example.fintech.DTO.UserDTO;
import com.example.fintech.DTO.CardDTO;
import com.example.fintech.DTO.UserCreationDTO;
import com.example.fintech.DTO.UserUpdateDTO;

import java.util.UUID;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {
	private final UserService userService;
	private final CardService cardService;

	public UserController(UserService userService, CardService cardService) {
		this.userService = userService;
		this.cardService = cardService;
	}

	@GetMapping
	public List<UserDTO> getAllUsers() {
		return userService.getAllUsers();
	}

	@GetMapping("/{id}")
	public UserDTO getUserById(@PathVariable UUID id) {
		return userService.getUserById(id);
	}

	@PostMapping
	public UserDTO createUser(@Valid @RequestBody UserCreationDTO dto) {
		return userService.createUser(dto);
	}

	@PatchMapping("/{id}")
	public UserDTO updateUser(@PathVariable UUID id, @Valid @RequestBody UserUpdateDTO dto) {
		return userService.updateUser(id, dto);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteUser(@PathVariable UUID id) {
		userService.deleteUser(id);
	}

	@GetMapping("/{id}/cards")
	public List<CardDTO> getAllUserCards(@PathVariable UUID id) {
		return cardService.getAllUserCards(id);
	}
}