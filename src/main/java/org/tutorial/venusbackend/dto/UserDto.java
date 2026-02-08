package org.tutorial.venusbackend.dto;

import java.time.LocalDateTime;

public record UserDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String address,
        String role,
        boolean isActive,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}