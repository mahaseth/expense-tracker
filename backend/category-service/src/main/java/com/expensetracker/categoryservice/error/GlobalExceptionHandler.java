package com.expensetracker.categoryservice.error;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import feign.FeignException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> notFound(ResourceNotFoundException ex) {
		return build(HttpStatus.NOT_FOUND, ex.getMessage());
	}

	@ExceptionHandler(ResourceConflictException.class)
	public ResponseEntity<?> conflict(ResourceConflictException ex) {
		return build(HttpStatus.CONFLICT, ex.getMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> badRequest(MethodArgumentNotValidException ex) {
		Map<String, String> fields = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(e -> fields.put(e.getField(), e.getDefaultMessage()));
		return build(HttpStatus.BAD_REQUEST, "Validation failed", fields);
	}

	@ExceptionHandler(FeignException.class)
	public ResponseEntity<?> feign(FeignException ex) {
		return build(HttpStatus.BAD_GATEWAY, "Upstream user-service error: " + ex.status());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> generic(Exception ex) {
		return build(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error");
	}

	private ResponseEntity<Map<String, Object>> build(HttpStatus status, String message) {
		return build(status, message, null);
	}

	private ResponseEntity<Map<String, Object>> build(HttpStatus status, String message, Object details) {
		Map<String, Object> body = new HashMap<>();
		body.put("timestamp", OffsetDateTime.now());
		body.put("status", status.value());
		body.put("error", status.getReasonPhrase());
		body.put("message", message);
		if (details != null)
			body.put("details", details);
		return ResponseEntity.status(status).body(body);
	}
}
