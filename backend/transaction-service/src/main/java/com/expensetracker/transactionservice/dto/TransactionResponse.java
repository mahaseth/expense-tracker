package com.expensetracker.transactionservice.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

import com.expensetracker.transactionservice.entity.TransactionType;

public record TransactionResponse(Long id, Long userId, TransactionType type, LocalDate date, BigDecimal amount,
		Long categoryId, Long subcategoryId, String note, String description, OffsetDateTime createdAt,
		OffsetDateTime updatedAt) {
}
