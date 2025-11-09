package com.expensetracker.categoryservice.dto;

import java.util.List;

public record CategoryTreeResponse(Long categoryId, String categoryName, List<SubcategoryResponse> subcategories) {
}
