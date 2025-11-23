package com.expensetracker.transactionservice.dto;

public record UserDto(Long id, String name, String email, String phone, String address) {
}
