package com.expensetracker.transactionservice.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "transactions", indexes = { @Index(name = "idx_tx_user_date", columnList = "user_id, date"),
		@Index(name = "idx_tx_user_type", columnList = "user_id, type") })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 16)
	private TransactionType type;

	@Column(nullable = false)
	private LocalDate date;

	@Column(nullable = false, precision = 18, scale = 2)
	private BigDecimal amount;

	@Column(name = "category_id", nullable = false)
	private Long categoryId;

	@Column(name = "subcategory_id")
	private Long subcategoryId; // nullable

	@Column(length = 255)
	private String note;

	@Column(length = 2000)
	private String description;

	@Column(nullable = false, updatable = false)
	private OffsetDateTime createdAt;

	@Column(nullable = false)
	private OffsetDateTime updatedAt;

	@PrePersist
	void prePersist() {
		createdAt = OffsetDateTime.now();
		updatedAt = createdAt;
	}

	@PreUpdate
	void preUpdate() {
		updatedAt = OffsetDateTime.now();
	}
}
