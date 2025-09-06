package br.com.ecommercechallenge.dto.cart;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductInCartDto(
        UUID id,
        String name,
        String description,
        BigDecimal price
) {}
