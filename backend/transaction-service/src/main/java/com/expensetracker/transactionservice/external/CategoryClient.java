package com.expensetracker.transactionservice.external;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.expensetracker.transactionservice.dto.CategoryDto;
import com.expensetracker.transactionservice.dto.SubcategoryDto;

@FeignClient(name = "Category-Service")
public interface CategoryClient {

	@GetMapping("/api/v1/categories/{id}")
	CategoryDto getCategory(@PathVariable("id") Long id);

	@GetMapping("/api/v1/categories/{categoryId}/subcategories")
	List<SubcategoryDto> listSubcategories(@PathVariable("categoryId") Long categoryId);

}
