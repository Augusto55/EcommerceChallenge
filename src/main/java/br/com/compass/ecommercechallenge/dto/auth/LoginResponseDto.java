package br.com.compass.ecommercechallenge.dto.auth;

public record LoginResponseDto(String accessToken, Long expiresIn) {
}
