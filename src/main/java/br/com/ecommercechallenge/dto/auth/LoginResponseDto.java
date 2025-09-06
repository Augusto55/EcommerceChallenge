package br.com.ecommercechallenge.dto.auth;

public record LoginResponseDto(String accessToken, Long expiresIn) {
}
