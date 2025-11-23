package com.expensetracker.transactionservice.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

import com.expensetracker.transactionservice.entity.TransactionType;

public record TransactionResponse(String id, UserDto user, TransactionType type, LocalDate date, BigDecimal amount,
		CategoryDto category, SubcategoryDto subcategory, String note, String description, OffsetDateTime createdAt,
		OffsetDateTime updatedAt) {
}
