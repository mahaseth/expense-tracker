package com.expensetracker.categoryservice.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateSubcategoryRequest(@NotBlank String name) {
}
