package com.expensetracker.categoryservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.expensetracker.categoryservice.entity.Subcategory;

public interface SubcategoryRepository extends JpaRepository<Subcategory, Long> {
	List<Subcategory> findByCategoryIdOrderByNameAsc(Long categoryId);

	boolean existsByCategoryIdAndNameIgnoreCase(Long categoryId, String name);

	Optional<Subcategory> findByCategoryIdAndNameIgnoreCase(Long categoryId, String name);
}
