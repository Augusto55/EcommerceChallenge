package br.com.ecommercechallenge.exception;

public class InactivateProductException extends RuntimeException {
    public InactivateProductException(String message) {
        super(message);
    }

    public InactivateProductException() { super("Product is inactive."); };

}
