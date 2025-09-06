package br.com.ecommercechallenge.dto.user;

import java.util.UUID;

public record UserOrderCountDto(
        UUID userId,
        String userName,
        String userEmail,
        Long orderCount
) {}
