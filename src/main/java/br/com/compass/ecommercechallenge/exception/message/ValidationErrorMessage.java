package br.com.compass.ecommercechallenge.exception.message;

import lombok.Builder;

import java.util.Map;

@Builder
public record ValidationErrorMessage(
        String title, int status, Map<String, String> details, String instance
) {
}
