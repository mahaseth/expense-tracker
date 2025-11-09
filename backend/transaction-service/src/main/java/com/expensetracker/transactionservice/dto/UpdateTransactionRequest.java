package com.expensetracker.transactionservice.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.expensetracker.transactionservice.entity.TransactionType;

public record UpdateTransactionRequest(TransactionType type, LocalDate date, BigDecimal amount, Long categoryId,
		Long subcategoryId, String note, String description) {
}
