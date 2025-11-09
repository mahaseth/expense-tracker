package com.expensetracker.transactionservice.external;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "categoryClient", url = "${CATEGORY-SERVICE}")
public interface CategoryClient {

	@GetMapping("/api/v1/categories/{id}")
	CategoryDto getCategory(@PathVariable("id") Long id);

	@GetMapping("/api/v1/categories/{categoryId}/subcategories")
	List<SubcategoryDto> listSubcategories(@PathVariable("categoryId") Long categoryId);

	record CategoryDto(Long id, Long userId, String name, String createdAt, String updatedAt) {
	}

	record SubcategoryDto(Long id, Long categoryId, String name, String createdAt, String updatedAt) {
	}
}
