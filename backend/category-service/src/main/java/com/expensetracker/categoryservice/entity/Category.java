package com.expensetracker.categoryservice.entity;

import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "categories", uniqueConstraints = @UniqueConstraint(name = "uk_user_name", columnNames = { "user_id",
		"name" }))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** Owner in user-service */
	@Column(name = "user_id", nullable = false)
	private Long userId;

	@NotBlank
	@Column(nullable = false)
	private String name;

	@Column(nullable = false, updatable = false)
	private OffsetDateTime createdAt;

	@Column(nullable = false)
	private OffsetDateTime updatedAt;

	@PrePersist
	void prePersist() {
		this.createdAt = OffsetDateTime.now();
		this.updatedAt = this.createdAt;
	}

	@PreUpdate
	void preUpdate() {
		this.updatedAt = OffsetDateTime.now();
	}
}
