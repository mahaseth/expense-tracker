package com.expensetracker.userservice.service;

import java.util.List;
import java.util.Optional;

import com.expensetracker.userservice.entity.User;

public interface UserService {
	User createUser(User user);
	List<User> getAllUsers();

	Optional<User> getUserById(Long id);

	User save(User user);
}
 