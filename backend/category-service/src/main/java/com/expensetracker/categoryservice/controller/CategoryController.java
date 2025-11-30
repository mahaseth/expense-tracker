package com.expensetracker.categoryservice.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.RequestParam;

import com.expensetracker.categoryservice.dto.CategoryResponse;
import com.expensetracker.categoryservice.dto.CategoryTreeResponse;
import com.expensetracker.categoryservice.dto.CreateCategoryRequest;
import com.expensetracker.categoryservice.dto.CreateSubcategoryRequest;
import com.expensetracker.categoryservice.dto.SubcategoryResponse;
import com.expensetracker.categoryservice.dto.UpdateCategoryRequest;
import com.expensetracker.categoryservice.dto.UpdateSubcategoryRequest;
import com.expensetracker.categoryservice.service.CategoryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/categories")
public class CategoryController {

	private final CategoryService service;

	// ----- Categories -----
	@PostMapping
	public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CreateCategoryRequest req) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.createCategory(req));
	}

	@PatchMapping("/{categoryId}")
	public ResponseEntity<CategoryResponse> updateCategory(@PathVariable Long categoryId,
			@Valid @RequestBody UpdateCategoryRequest req) {
		return ResponseEntity.ok(service.updateCategory(categoryId, req));
	}

	@DeleteMapping("/{categoryId}")
	public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId) {
		service.deleteCategory(categoryId);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{categoryId}")
	public ResponseEntity<CategoryResponse> getCategory(@PathVariable Long categoryId) {
		return ResponseEntity.ok(service.getCategory(categoryId));
	}

	@GetMapping("/by-user/{userId}")
	public ResponseEntity<List<CategoryResponse>> listCategoriesByUser(@PathVariable Long userId) {
		return ResponseEntity.ok(service.listCategoriesByUser(userId));
	}

	@GetMapping("/by-user/{userId}/tree")
	public ResponseEntity<List<CategoryTreeResponse>> listCategoryTree(@PathVariable Long userId) {
		return ResponseEntity.ok(service.listCategoryTreeByUser(userId));
	}
	
	@GetMapping
	public ResponseEntity<List<CategoryResponse>> getCategories(@RequestParam List<Long> ids) {
		return ResponseEntity.ok(service.getCategoriesByIds(ids));
	}

	@GetMapping("/subcategories")
	public ResponseEntity<List<SubcategoryResponse>> getSubcategories(@RequestParam List<Long> ids) {
		return ResponseEntity.ok(service.getSubcategoriesByIds(ids));
	}

	// ----- Subcategories -----
	@PostMapping("/{categoryId}/subcategories")
	public ResponseEntity<SubcategoryResponse> createSubcategory(@PathVariable Long categoryId,
			@Valid @RequestBody CreateSubcategoryRequest req) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.createSubcategory(categoryId, req));
	}

	@GetMapping("/{categoryId}/subcategories")
	public ResponseEntity<List<SubcategoryResponse>> listSubcategories(@PathVariable Long categoryId) {
		return ResponseEntity.ok(service.listSubcategories(categoryId));
	}

	@PatchMapping("/subcategories/{subcategoryId}")
	public ResponseEntity<SubcategoryResponse> updateSubcategory(@PathVariable Long subcategoryId,
			@Valid @RequestBody UpdateSubcategoryRequest req) {
		return ResponseEntity.ok(service.updateSubcategory(subcategoryId, req));
	}

	@DeleteMapping("/subcategories/{subcategoryId}")
	public ResponseEntity<Void> deleteSubcategory(@PathVariable Long subcategoryId) {
		service.deleteSubcategory(subcategoryId);
		return ResponseEntity.noContent().build();
	}
}
