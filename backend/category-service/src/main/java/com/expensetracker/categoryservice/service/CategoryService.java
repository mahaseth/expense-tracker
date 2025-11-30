package com.expensetracker.categoryservice.service;

import java.util.List;

import com.expensetracker.categoryservice.dto.*;

public interface CategoryService {
	CategoryResponse createCategory(CreateCategoryRequest req);

	CategoryResponse updateCategory(Long categoryId, UpdateCategoryRequest req);

	void deleteCategory(Long categoryId);

	CategoryResponse getCategory(Long categoryId);

	List<CategoryResponse> listCategoriesByUser(Long userId);

	List<CategoryTreeResponse> listCategoryTreeByUser(Long userId);

	SubcategoryResponse createSubcategory(Long categoryId, CreateSubcategoryRequest req);

	SubcategoryResponse updateSubcategory(Long subcategoryId, UpdateSubcategoryRequest req);

	void deleteSubcategory(Long subcategoryId);

	List<SubcategoryResponse> listSubcategories(Long categoryId);
	
	List<CategoryResponse> getCategoriesByIds(List<Long> ids);
	
	List<SubcategoryResponse> getSubcategoriesByIds(List<Long> ids);
}
