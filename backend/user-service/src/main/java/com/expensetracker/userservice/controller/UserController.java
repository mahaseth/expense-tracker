package com.expensetracker.userservice.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.expensetracker.userservice.entity.User;
import com.expensetracker.userservice.service.UserService;

@RestController
@RequestMapping("api/v1/user")
public class UserController {
	
	@Autowired UserService userService;

	@PostMapping
	public ResponseEntity<User> createUser(@RequestBody User user) {
		return ResponseEntity.ok(userService.createUser(user));
	}

	@GetMapping("/all")
	public ResponseEntity<List<User>> getAllUsers() {
		return ResponseEntity.ok(userService.getAllUsers());
	}

	@GetMapping("/{id}")
	public ResponseEntity<User> getUserById(@PathVariable Long id) {
		return userService.getUserById(id).map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).<User>build());
	}

	@PutMapping("/{id}")
	public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User update) {
		return userService.getUserById(id) // Optional<User>
				.map(existing -> {
					existing.setName(update.getName());
					existing.setEmail(update.getEmail());
					existing.setPhone(update.getPhone());
					existing.setAddress(update.getAddress());
					return userService.save(existing); // User
				}).map(ResponseEntity::ok) // ResponseEntity<User>
				.orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}




}
