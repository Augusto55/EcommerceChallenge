package br.com.compass.ecommercechallenge.exception;

public class SamePasswordException extends RuntimeException {
    public SamePasswordException(String message) {
        super(message);
    }
    public SamePasswordException() {
        super("Password cannot be the same as previous password");
    }
}
