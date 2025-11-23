package com.expensetracker.transactionservice.service;

import java.time.LocalDate;
import java.util.List;

import com.expensetracker.transactionservice.dto.CreateTransactionRequest;
import com.expensetracker.transactionservice.dto.TransactionResponse;
import com.expensetracker.transactionservice.dto.UpdateTransactionRequest;
import com.expensetracker.transactionservice.entity.TransactionType;

public interface TransactionService {
	TransactionResponse create(CreateTransactionRequest req);

	TransactionResponse getById(String id);

	TransactionResponse update(String id, UpdateTransactionRequest req);

	void delete(String id);

	List<TransactionResponse> listByUser(Long userId, TransactionType type, LocalDate from, LocalDate to,
			Long categoryId);
}
