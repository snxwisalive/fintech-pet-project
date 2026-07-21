package com.example.fintech.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.Optional;

import com.example.fintech.model.User;

public interface UserRepository extends JpaRepository<User, UUID> {
	Optional<User> findByPhoneNumber(String phoneNumber);

	Optional<User> findByEmail(String email);
}