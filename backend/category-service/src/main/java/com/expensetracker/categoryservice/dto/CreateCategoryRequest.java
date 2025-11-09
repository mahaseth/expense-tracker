package com.expensetracker.categoryservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCategoryRequest(@NotNull Long userId, @NotBlank String name) {
}
