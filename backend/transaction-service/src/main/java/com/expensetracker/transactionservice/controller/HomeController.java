package com.expensetracker.transactionservice.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
	@GetMapping("/")
	public ResponseEntity<Map<String, Object>> home() {
		return ResponseEntity.ok(Map.of("service", "transaction-service", "status", "ok"));
	}

	@GetMapping("/__ping")
	public String ping() {
		return "pong";
	}
}
