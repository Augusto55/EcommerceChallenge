package br.com.compass.ecommercechallenge.dto;

import jakarta.validation.constraints.Size;

public record ResetPasswordDto(
        String token,
        @Size(min = 3) String password) {
}
