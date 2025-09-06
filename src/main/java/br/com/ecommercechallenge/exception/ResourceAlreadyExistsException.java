package br.com.ecommercechallenge.exception;

public class ResourceAlreadyExistsException extends RuntimeException {
    public ResourceAlreadyExistsException() {
        super("Resource already exists.");
    }

    public ResourceAlreadyExistsException(String resourceName, String identifier) {
        super(resourceName + " with this " + identifier + " already exists.");
    }

    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}
