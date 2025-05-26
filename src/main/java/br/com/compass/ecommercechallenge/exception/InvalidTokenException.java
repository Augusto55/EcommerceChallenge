package br.com.compass.ecommercechallenge.exception;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {
        super(message);
    }
    public InvalidTokenException() {
        super("The token is invalid or expired");
    }
}
