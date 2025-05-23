package br.com.compass.ecommercechallenge.exception;

import org.springframework.http.HttpStatus;

public record RestErrorMessage(
        String message
) {
}
