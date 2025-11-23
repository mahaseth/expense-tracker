package com.expensetracker.transactionservice.dto;

public record CategoryDto(Long id, Long userId, String name, String createdAt, String updatedAt) {
}
