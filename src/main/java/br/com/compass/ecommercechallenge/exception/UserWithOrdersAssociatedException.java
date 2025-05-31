package br.com.compass.ecommercechallenge.exception;

public class UserWithOrdersAssociatedException extends RuntimeException {
    public UserWithOrdersAssociatedException(String message) {
        super(message);
    }
    public UserWithOrdersAssociatedException() {super("Cannot delete user because it has orders associated");}
}
