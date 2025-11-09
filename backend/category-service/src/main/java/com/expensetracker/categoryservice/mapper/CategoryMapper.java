package com.expensetracker.categoryservice.mapper;

import org.springframework.stereotype.Component;

import com.expensetracker.categoryservice.dto.CategoryResponse;
import com.expensetracker.categoryservice.dto.CreateCategoryRequest;
import com.expensetracker.categoryservice.entity.Category;

@Component
public class CategoryMapper {
	public Category toEntity(CreateCategoryRequest req) {
		return Category.builder().userId(req.userId()).name(req.name().trim()).build();
	}

	public CategoryResponse toResponse(Category c) {
		return new CategoryResponse(c.getId(), c.getUserId(), c.getName(), c.getCreatedAt(), c.getUpdatedAt());
	}
}
