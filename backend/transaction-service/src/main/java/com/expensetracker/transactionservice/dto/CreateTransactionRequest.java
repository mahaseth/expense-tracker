package com.expensetracker.transactionservice.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.expensetracker.transactionservice.entity.TransactionType;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateTransactionRequest(@NotNull Long userId, @NotNull TransactionType type, @NotNull LocalDate date,
		@NotNull @DecimalMin(value = "0.00", inclusive = false) BigDecimal amount, @NotNull Long categoryId,
		Long subcategoryId, // optional
		@Size(max = 255) String note, @Size(max = 2000) String description) {
}
