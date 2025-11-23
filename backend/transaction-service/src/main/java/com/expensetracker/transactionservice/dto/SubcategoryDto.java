package com.expensetracker.transactionservice.dto;

public record SubcategoryDto(Long id, Long categoryId, String name, String createdAt, String updatedAt) {
}
