package com.expensetracker.categoryservice.error;

public class ResourceConflictException extends RuntimeException {
	public ResourceConflictException(String msg) {
		super(msg);
	}
}
