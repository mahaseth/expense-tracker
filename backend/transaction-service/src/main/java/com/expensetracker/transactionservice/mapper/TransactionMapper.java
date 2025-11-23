package com.expensetracker.transactionservice.mapper;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.expensetracker.transactionservice.dto.CategoryDto;
import com.expensetracker.transactionservice.dto.CreateTransactionRequest;
import com.expensetracker.transactionservice.dto.SubcategoryDto;
import com.expensetracker.transactionservice.dto.TransactionResponse;
import com.expensetracker.transactionservice.dto.UserDto;
import com.expensetracker.transactionservice.entity.Transaction;
import com.expensetracker.transactionservice.external.CategoryClient;
import com.expensetracker.transactionservice.external.UserClient;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TransactionMapper {

	private final UserClient userClient;
	private final CategoryClient categoryClient;

	public Transaction toEntity(CreateTransactionRequest req) {
		return Transaction.builder().userId(req.userId()).type(req.type()).date(req.date()).amount(req.amount())
				.categoryId(req.categoryId()).subcategoryId(req.subcategoryId()).note(req.note())
				.description(req.description()).build();
	}

	public TransactionResponse toResponse(Transaction t) {
		UserDto userDto = userClient.getUserById(t.getUserId());
		CategoryDto categoryDto = categoryClient.getCategory(t.getCategoryId());
		SubcategoryDto subcategoryDto = null;
		if (t.getSubcategoryId() != null) {
			List<SubcategoryDto> subcategoryDtos = categoryClient.listSubcategories(t.getCategoryId());
			Optional<SubcategoryDto> matchingSubcategory = subcategoryDtos.stream()
					.filter(sc -> sc.id().equals(t.getSubcategoryId())).findFirst();
			subcategoryDto = matchingSubcategory.orElse(null);
		}

		return new TransactionResponse(t.getId(), userDto, t.getType(), t.getDate(), t.getAmount(),
				categoryDto, subcategoryDto, t.getNote(), t.getDescription(), t.getCreatedAt(),
				t.getUpdatedAt());
	}
}

