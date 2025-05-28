package br.com.compass.ecommercechallenge.dto.product;

import jakarta.validation.constraints.NotNull;

public record ProductActiveDto(@NotNull Boolean active) {}

