package com.expensetracker.categoryservice.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "User-Service")
public interface UserClient {
	@GetMapping("/api/v1/user/{id}")
	UserDto getUserById(@PathVariable("id") Long id);

	record UserDto(Long id, String name, String email, String phone, String address) {
	}
}
