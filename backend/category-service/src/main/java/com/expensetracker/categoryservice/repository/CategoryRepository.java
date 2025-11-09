package com.expensetracker.categoryservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.expensetracker.categoryservice.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
	List<Category> findByUserIdOrderByNameAsc(Long userId);

	Optional<Category> findByUserIdAndNameIgnoreCase(Long userId, String name);

	boolean existsByUserIdAndNameIgnoreCase(Long userId, String name);
}
