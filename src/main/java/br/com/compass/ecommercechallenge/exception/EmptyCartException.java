package br.com.compass.ecommercechallenge.exception;

public class EmptyCartException extends RuntimeException {
    public EmptyCartException(String message) {
        super(message);
    }

    public EmptyCartException() { super("The cart is empty"); }
}
