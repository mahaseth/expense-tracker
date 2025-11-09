package com.expensetracker.transactionservice.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.expensetracker.transactionservice.dto.CreateTransactionRequest;
import com.expensetracker.transactionservice.dto.TransactionResponse;
import com.expensetracker.transactionservice.dto.UpdateTransactionRequest;
import com.expensetracker.transactionservice.entity.TransactionType;
import com.expensetracker.transactionservice.service.TransactionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/transactions")
public class TransactionController {

	private final TransactionService service;

	@PostMapping
	public ResponseEntity<TransactionResponse> create(@Valid @RequestBody CreateTransactionRequest req) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.create(req));
	}

	@GetMapping("/{id}")
	public ResponseEntity<TransactionResponse> get(@PathVariable Long id) {
		return ResponseEntity.ok(service.getById(id));
	}

	@PatchMapping("/{id}")
	public ResponseEntity<TransactionResponse> update(@PathVariable Long id,
			@RequestBody UpdateTransactionRequest req) {
		return ResponseEntity.ok(service.update(id, req));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/by-user/{userId}")
	public ResponseEntity<List<TransactionResponse>> listByUser(@PathVariable Long userId,
			@RequestParam(required = false) TransactionType type,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
			@RequestParam(required = false) Long categoryId) {
		return ResponseEntity.ok(service.listByUser(userId, type, from, to, categoryId));
	}
}
