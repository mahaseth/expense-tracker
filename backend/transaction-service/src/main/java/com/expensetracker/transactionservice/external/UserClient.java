package com.expensetracker.transactionservice.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.expensetracker.transactionservice.config.FeignAuthConfig;
import com.expensetracker.transactionservice.dto.UserDto;

@FeignClient(name = "User-Service", configuration = FeignAuthConfig.class)
public interface UserClient {
	@GetMapping("/api/v1/user/{id}")
	UserDto getUserById(@PathVariable("id") Long id);

}
