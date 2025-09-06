package br.com.ecommercechallenge.exception;

public class InvalidUuidFormatException extends RuntimeException {
    public InvalidUuidFormatException() {
        super("Invalid UUID format.");
    }


    public InvalidUuidFormatException(String message) {
        super(message);
    }
}
