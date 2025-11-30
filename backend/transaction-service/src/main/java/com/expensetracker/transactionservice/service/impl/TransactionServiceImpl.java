package com.expensetracker.transactionservice.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.expensetracker.transactionservice.dto.CreateTransactionRequest;
import com.expensetracker.transactionservice.dto.TransactionResponse;
import com.expensetracker.transactionservice.dto.UpdateTransactionRequest;
import com.expensetracker.transactionservice.entity.Transaction;
import com.expensetracker.transactionservice.entity.TransactionType;
import com.expensetracker.transactionservice.error.BadRequestException;
import com.expensetracker.transactionservice.error.ResourceNotFoundException;
import com.expensetracker.transactionservice.external.CategoryClient;
import com.expensetracker.transactionservice.external.UserClient;
import com.expensetracker.transactionservice.mapper.TransactionMapper;
import com.expensetracker.transactionservice.repository.TransactionRepository;
import com.expensetracker.transactionservice.service.TransactionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionServiceImpl implements TransactionService {

	private final TransactionRepository repo;
	private final TransactionMapper mapper;
	private final UserClient userClient;
	private final CategoryClient categoryClient;

	@Override
	public TransactionResponse create(CreateTransactionRequest req) {
		// 1) user must exist
		var user = userClient.getUserById(req.userId());

		// 2) category must exist and belong to same user
		var cat = categoryClient.getCategory(req.categoryId());
		if (!cat.userId().equals(user.id())) {
			throw new BadRequestException("Category does not belong to user");
		}

		// 3) if subcategoryId present, ensure it's under this category
		if (req.subcategoryId() != null) {
			boolean ok = categoryClient.listSubcategories(req.categoryId()).stream()
					.anyMatch(sc -> sc.id().equals(req.subcategoryId()));
			if (!ok)
				throw new BadRequestException("Subcategory does not belong to the category");
		}

		Transaction saved = repo.save(mapper.toEntity(req));
		return mapper.toResponse(saved);
	}

	@Override
	@Transactional(readOnly = true)
	public TransactionResponse getById(String id) {
		Transaction t = repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Transaction not found: " + id));
		return mapper.toResponse(t);
	}

	@Override
	public TransactionResponse update(String id, UpdateTransactionRequest req) {
		Transaction t = repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Transaction not found: " + id));

		if (req.type() != null)
			t.setType(req.type());
		if (req.date() != null)
			t.setDate(req.date());
		if (req.amount() != null)
			t.setAmount(req.amount());

		if (req.categoryId() != null) {
			var cat = categoryClient.getCategory(req.categoryId());
			if (!cat.userId().equals(t.getUserId())) {
				throw new BadRequestException("New category does not belong to this user");
			}
			t.setCategoryId(req.categoryId());
			// If category changed and subcategory remains set, ensure it still matches
			// (optional)
			if (t.getSubcategoryId() != null) {
				boolean ok = categoryClient.listSubcategories(req.categoryId()).stream()
						.anyMatch(sc -> sc.id().equals(t.getSubcategoryId()));
				if (!ok)
					t.setSubcategoryId(null);
			}
		}

		if (req.subcategoryId() != null) {
			boolean ok = categoryClient.listSubcategories(t.getCategoryId()).stream()
					.anyMatch(sc -> sc.id().equals(req.subcategoryId()));
			if (!ok)
				throw new BadRequestException("Subcategory not under current category");
			t.setSubcategoryId(req.subcategoryId());
		}

		if (req.note() != null)
			t.setNote(req.note());
		if (req.description() != null)
			t.setDescription(req.description());

		return mapper.toResponse(repo.save(t));
	}

	@Override
	public void delete(String id) {
		if (!repo.existsById(id))
			throw new ResourceNotFoundException("Transaction not found: " + id);
		repo.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<TransactionResponse> listByUser(Long userId, TransactionType type, LocalDate from, LocalDate to,
			Long categoryId) {
		List<Transaction> list;
		if (categoryId != null && from != null && to != null) {
			list = repo.findByUserIdAndCategoryIdAndDateBetweenOrderByDateDesc(userId, categoryId, from, to);
		} else if (categoryId != null) {
			list = repo.findByUserIdAndCategoryIdOrderByDateDesc(userId, categoryId);
		} else if (type != null && from != null && to != null) {
			list = repo.findByUserIdAndTypeAndDateBetweenOrderByDateDesc(userId, type, from, to);
		} else if (from != null && to != null) {
			list = repo.findByUserIdAndDateBetweenOrderByDateDesc(userId, from, to);
		} else if (type != null) {
			list = repo.findByUserIdAndTypeOrderByDateDesc(userId, type);
		} else {
			list = repo.findByUserIdOrderByDateDesc(userId);
		}
		return list.stream().map(mapper::toResponse).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<TransactionResponse> listByUserOptimized(Long userId, TransactionType type, LocalDate from,
			LocalDate to, Long categoryId) {
		List<Transaction> list;
		if (categoryId != null && from != null && to != null) {
			list = repo.findByUserIdAndCategoryIdAndDateBetweenOrderByDateDesc(userId, categoryId, from, to);
		} else if (categoryId != null) {
			list = repo.findByUserIdAndCategoryIdOrderByDateDesc(userId, categoryId);
		} else if (type != null && from != null && to != null) {
			list = repo.findByUserIdAndTypeAndDateBetweenOrderByDateDesc(userId, type, from, to);
		} else if (from != null && to != null) {
			list = repo.findByUserIdAndDateBetweenOrderByDateDesc(userId, from, to);
		} else if (type != null) {
			list = repo.findByUserIdAndTypeOrderByDateDesc(userId, type);
		} else {
			list = repo.findByUserIdOrderByDateDesc(userId);
		}

		if (list.isEmpty()) {
			return List.of();
		}

		var user = userClient.getUserById(userId);

		List<Long> categoryIds = list.stream().map(Transaction::getCategoryId).distinct().toList();
		List<Long> subcategoryIds = list.stream().filter(t -> t.getSubcategoryId() != null)
				.map(Transaction::getSubcategoryId).distinct().toList();

		var categories = categoryClient.getCategories(categoryIds);
		var subcategories = categoryClient.getSubcategories(subcategoryIds);

		return list.stream().map(t -> mapper.toResponse(t, user, categories, subcategories)).toList();
	}
}
