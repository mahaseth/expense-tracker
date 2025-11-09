package com.expensetracker.transactionservice.error;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> notFound(ResourceNotFoundException ex) {
		return build(HttpStatus.NOT_FOUND, ex.getMessage(), null, ex);
	}

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<?> badRequest(BadRequestException ex) {
		return build(HttpStatus.BAD_REQUEST, ex.getMessage(), null, ex);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> validation(MethodArgumentNotValidException ex) {
		Map<String, String> fields = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(e -> fields.put(e.getField(), e.getDefaultMessage()));
		return build(HttpStatus.BAD_REQUEST, "Validation failed", fields, ex);
	}

	// JSON parse / enum / date errors show up here
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<?> unreadable(HttpMessageNotReadableException ex) {
		String hint = (ex.getMostSpecificCause() != null) ? ex.getMostSpecificCause().getMessage() : ex.getMessage();
		return build(HttpStatus.BAD_REQUEST, "Malformed request body: " + hint, null, ex);
	}

	@ExceptionHandler({ NoResourceFoundException.class, NoHandlerFoundException.class })
	public ResponseEntity<?> notFound404(Exception ex) {
		return build(HttpStatus.NOT_FOUND, "Resource not found", null, ex);
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<?> methodNotAllowed(HttpRequestMethodNotSupportedException ex) {
		return build(HttpStatus.METHOD_NOT_ALLOWED, "Method not allowed", null, ex);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<?> badParam(MethodArgumentTypeMismatchException ex) {
		return build(HttpStatus.BAD_REQUEST, "Invalid parameter '" + ex.getName() + "'", null, ex);
	}

	// Upstream calls (user/category)
	@ExceptionHandler(FeignException.NotFound.class)
	public ResponseEntity<?> upstream404(FeignException.NotFound ex) {
		return build(HttpStatus.NOT_FOUND, "Upstream resource not found", null, ex);
	}

	@ExceptionHandler(FeignException.class)
	public ResponseEntity<?> upstream(FeignException ex) {
		return build(HttpStatus.BAD_GATEWAY, "Upstream service error: " + ex.status(), null, ex);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> generic(Exception ex) {
		return build(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error: " + ex.getMessage(), null, ex);
	}

	private ResponseEntity<Map<String, Object>> build(HttpStatus status, String message, Object details, Exception ex) {
		// log full stack for troubleshooting
		log.error("[{}] {}", status.value(), message, ex);
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
