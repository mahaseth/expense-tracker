package com.expensetracker.transactionservice.external;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestParam;

import com.expensetracker.transactionservice.dto.CategoryDto;
import com.expensetracker.transactionservice.dto.SubcategoryDto;
import com.expensetracker.transactionservice.config.FeignAuthConfig;

@FeignClient(name = "Category-Service", configuration = FeignAuthConfig.class)
public interface CategoryClient {

	@GetMapping("/api/v1/categories/{id}")
	CategoryDto getCategory(@PathVariable("id") Long id);

	@GetMapping("/api/v1/categories/{categoryId}/subcategories")
	List<SubcategoryDto> listSubcategories(@PathVariable("categoryId") Long categoryId);

	@GetMapping("/api/v1/categories")
	List<CategoryDto> getCategories(@RequestParam("ids") List<Long> ids);

	@GetMapping("/api/v1/categories/subcategories")
	List<SubcategoryDto> getSubcategories(@RequestParam("ids") List<Long> ids);

}
