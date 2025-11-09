package com.expensetracker.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.expensetracker.userservice.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
