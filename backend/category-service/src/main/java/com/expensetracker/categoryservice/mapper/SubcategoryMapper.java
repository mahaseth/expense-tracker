package com.expensetracker.categoryservice.mapper;

import org.springframework.stereotype.Component;

import com.expensetracker.categoryservice.dto.SubcategoryResponse;
import com.expensetracker.categoryservice.entity.Subcategory;

@Component
public class SubcategoryMapper {
	public SubcategoryResponse toResponse(Subcategory s) {
		return new SubcategoryResponse(s.getId(), s.getCategoryId(), s.getName(), s.getCreatedAt(), s.getUpdatedAt());
	}
}
