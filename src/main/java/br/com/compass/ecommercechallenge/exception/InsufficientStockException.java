package br.com.compass.ecommercechallenge.exception;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String message) {
        super(message);
    }

    public InsufficientStockException() { super("Insufficient stock from selected product."); }
}
