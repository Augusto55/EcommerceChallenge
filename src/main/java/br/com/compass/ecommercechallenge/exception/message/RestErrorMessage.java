package br.com.compass.ecommercechallenge.exception.message;

import lombok.Builder;

@Builder
public record RestErrorMessage(
        String title, int status, String detail, String instance
) {
}
