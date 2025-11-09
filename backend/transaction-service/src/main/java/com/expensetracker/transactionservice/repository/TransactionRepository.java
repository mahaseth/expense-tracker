package com.expensetracker.transactionservice.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.expensetracker.transactionservice.entity.Transaction;
import com.expensetracker.transactionservice.entity.TransactionType;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	List<Transaction> findByUserIdOrderByDateDesc(Long userId);

	List<Transaction> findByUserIdAndTypeOrderByDateDesc(Long userId, TransactionType type);

	List<Transaction> findByUserIdAndDateBetweenOrderByDateDesc(Long userId, LocalDate from, LocalDate to);

	List<Transaction> findByUserIdAndTypeAndDateBetweenOrderByDateDesc(Long userId, TransactionType type,
			LocalDate from, LocalDate to);

	List<Transaction> findByUserIdAndCategoryIdOrderByDateDesc(Long userId, Long categoryId);

	List<Transaction> findByUserIdAndCategoryIdAndDateBetweenOrderByDateDesc(Long userId, Long categoryId,
			LocalDate from, LocalDate to);
}
