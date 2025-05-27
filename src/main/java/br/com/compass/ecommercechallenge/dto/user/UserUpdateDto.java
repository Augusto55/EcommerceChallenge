package br.com.compass.ecommercechallenge.dto.user;

import jakarta.validation.constraints.Size;

public record UserUpdateDto(
        @Size(min = 2, max = 100) String name,
        @Size(min = 3) String password) {
}
