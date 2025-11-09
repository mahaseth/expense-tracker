package com.expensetracker.categoryservice.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.expensetracker.categoryservice.dto.CategoryResponse;
import com.expensetracker.categoryservice.dto.CategoryTreeResponse;
import com.expensetracker.categoryservice.dto.CreateCategoryRequest;
import com.expensetracker.categoryservice.dto.CreateSubcategoryRequest;
import com.expensetracker.categoryservice.dto.SubcategoryResponse;
import com.expensetracker.categoryservice.dto.UpdateCategoryRequest;
import com.expensetracker.categoryservice.dto.UpdateSubcategoryRequest;
import com.expensetracker.categoryservice.entity.Category;
import com.expensetracker.categoryservice.entity.Subcategory;
import com.expensetracker.categoryservice.error.ResourceConflictException;
import com.expensetracker.categoryservice.error.ResourceNotFoundException;
import com.expensetracker.categoryservice.external.UserClient;
import com.expensetracker.categoryservice.mapper.CategoryMapper;
import com.expensetracker.categoryservice.mapper.SubcategoryMapper;
import com.expensetracker.categoryservice.repository.CategoryRepository;
import com.expensetracker.categoryservice.repository.SubcategoryRepository;
import com.expensetracker.categoryservice.service.CategoryService;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

	private final CategoryRepository categoryRepo;
	private final SubcategoryRepository subcategoryRepo;
	private final CategoryMapper categoryMapper;
	private final SubcategoryMapper subcategoryMapper;
	private final UserClient userClient;

	@Override
	public CategoryResponse createCategory(CreateCategoryRequest req) {
		try {
			userClient.getUserById(req.userId());
		} catch (FeignException.NotFound nf) {
			throw new ResourceNotFoundException("User not found: " + req.userId());
		}

		if (categoryRepo.existsByUserIdAndNameIgnoreCase(req.userId(), req.name())) {
			throw new ResourceConflictException("Category already exists for this user");
		}
		Category saved = categoryRepo.save(categoryMapper.toEntity(req));
		return categoryMapper.toResponse(saved);
	}

	@Override
	public CategoryResponse updateCategory(Long categoryId, UpdateCategoryRequest req) {
		Category c = categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found: " + categoryId));

		if (req.name() != null && !req.name().isBlank()) {
			String newName = req.name().trim();
			if (!newName.equalsIgnoreCase(c.getName())
					&& categoryRepo.existsByUserIdAndNameIgnoreCase(c.getUserId(), newName)) {
				throw new ResourceConflictException("Another category with this name exists for the user");
			}
			c.setName(newName);
		}
		return categoryMapper.toResponse(categoryRepo.save(c));
	}

	@Override
	public void deleteCategory(Long categoryId) {
		if (!categoryRepo.existsById(categoryId)) {
			throw new ResourceNotFoundException("Category not found: " + categoryId);
		}
		subcategoryRepo.findByCategoryIdOrderByNameAsc(categoryId)
				.forEach(sc -> subcategoryRepo.deleteById(sc.getId()));
		categoryRepo.deleteById(categoryId);
	}

	@Override
	@Transactional(readOnly = true)
	public CategoryResponse getCategory(Long categoryId) {
		Category c = categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found: " + categoryId));
		return categoryMapper.toResponse(c);
	}

	@Override
	@Transactional(readOnly = true)
	public List<CategoryResponse> listCategoriesByUser(Long userId) {
		return categoryRepo.findByUserIdOrderByNameAsc(userId).stream().map(categoryMapper::toResponse).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<CategoryTreeResponse> listCategoryTreeByUser(Long userId) {
		List<Category> categories = categoryRepo.findByUserIdOrderByNameAsc(userId);
		if (categories.isEmpty())
			return List.of();

		Map<Long, List<SubcategoryResponse>> bucket = new HashMap<>();
		for (Category c : categories) {
			List<SubcategoryResponse> subs = subcategoryRepo.findByCategoryIdOrderByNameAsc(c.getId()).stream()
					.map(subcategoryMapper::toResponse).toList();
			bucket.put(c.getId(), subs);
		}

		return categories.stream()
				.map(c -> new CategoryTreeResponse(c.getId(), c.getName(), bucket.getOrDefault(c.getId(), List.of())))
				.toList();
	}

	@Override
	public SubcategoryResponse createSubcategory(Long categoryId, CreateSubcategoryRequest req) {
		Category parent = categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found: " + categoryId));

		if (subcategoryRepo.existsByCategoryIdAndNameIgnoreCase(categoryId, req.name())) {
			throw new ResourceConflictException("Subcategory already exists under this category");
		}
		Subcategory saved = subcategoryRepo
				.save(Subcategory.builder().categoryId(parent.getId()).name(req.name().trim()).build());
		return subcategoryMapper.toResponse(saved);
	}

	@Override
	public SubcategoryResponse updateSubcategory(Long subcategoryId, UpdateSubcategoryRequest req) {
		Subcategory s = subcategoryRepo.findById(subcategoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Subcategory not found: " + subcategoryId));

		if (req.name() != null && !req.name().isBlank()) {
			String newName = req.name().trim();
			if (!newName.equalsIgnoreCase(s.getName())
					&& subcategoryRepo.existsByCategoryIdAndNameIgnoreCase(s.getCategoryId(), newName)) {
				throw new ResourceConflictException("Another subcategory with this name exists in the category");
			}
			s.setName(newName);
		}
		return subcategoryMapper.toResponse(subcategoryRepo.save(s));
	}

	@Override
	public void deleteSubcategory(Long subcategoryId) {
		if (!subcategoryRepo.existsById(subcategoryId)) {
			throw new ResourceNotFoundException("Subcategory not found: " + subcategoryId);
		}
		subcategoryRepo.deleteById(subcategoryId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<SubcategoryResponse> listSubcategories(Long categoryId) {
		if (!categoryRepo.existsById(categoryId)) {
			throw new ResourceNotFoundException("Category not found: " + categoryId);
		}
		return subcategoryRepo.findByCategoryIdOrderByNameAsc(categoryId).stream().map(subcategoryMapper::toResponse)
				.toList();
	}
}
