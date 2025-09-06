package br.com.ecommercechallenge.exception;

public class ProductAssociatedWithOrderException extends RuntimeException {
    public ProductAssociatedWithOrderException(String productId) {
        super("Product with ID " + productId + " cannot be deleted because it is associated with one or more orders.");
    }
}
