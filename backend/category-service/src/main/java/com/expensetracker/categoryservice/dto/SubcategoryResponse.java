package com.expensetracker.categoryservice.dto;

import java.time.OffsetDateTime;

public record SubcategoryResponse(Long id, Long categoryId, String name, OffsetDateTime createdAt,
		OffsetDateTime updatedAt) {
}
