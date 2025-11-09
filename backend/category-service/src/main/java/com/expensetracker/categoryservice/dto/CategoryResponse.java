package com.expensetracker.categoryservice.dto;

import java.time.OffsetDateTime;

public record CategoryResponse(Long id, Long userId, String name, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
}
