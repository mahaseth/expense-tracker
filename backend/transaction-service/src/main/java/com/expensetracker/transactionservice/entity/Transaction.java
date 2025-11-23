package com.expensetracker.transactionservice.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transaction {

	@Id
	private String id;
	private Long userId;
	private TransactionType type;
	private LocalDate date;
	private BigDecimal amount;
	private Long categoryId;
	private Long subcategoryId;
	private String note;
	private String description;
	private OffsetDateTime createdAt;
	private OffsetDateTime updatedAt;
}
