package br.com.compass.ecommercechallenge.dto.product;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductResponseDto(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        Integer quantity,
        Boolean active
) {
}
