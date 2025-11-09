package com.expensetracker.transactionservice.mapper;

import org.springframework.stereotype.Component;

import com.expensetracker.transactionservice.dto.CreateTransactionRequest;
import com.expensetracker.transactionservice.dto.TransactionResponse;
import com.expensetracker.transactionservice.entity.Transaction;

@Component
public class TransactionMapper {

	public Transaction toEntity(CreateTransactionRequest req) {
		return Transaction.builder().userId(req.userId()).type(req.type()).date(req.date()).amount(req.amount())
				.categoryId(req.categoryId()).subcategoryId(req.subcategoryId()).note(req.note())
				.description(req.description()).build();
	}

	public TransactionResponse toResponse(Transaction t) {
		return new TransactionResponse(t.getId(), t.getUserId(), t.getType(), t.getDate(), t.getAmount(),
				t.getCategoryId(), t.getSubcategoryId(), t.getNote(), t.getDescription(), t.getCreatedAt(),
				t.getUpdatedAt());
	}
}
