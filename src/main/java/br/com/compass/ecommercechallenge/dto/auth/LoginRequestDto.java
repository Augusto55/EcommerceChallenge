package br.com.compass.ecommercechallenge.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequestDto(
        @NotBlank @Email @Size(max = 255) String email,
        @NotBlank @Size(min = 3) String password) {}
