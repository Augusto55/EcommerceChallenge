package br.com.compass.ecommercechallenge.model;

public enum UserTypeEnum {
    ADMIN("Administrator"),
    USER("User");

    public final String label;

    UserTypeEnum(String label) {
        this.label = label;
    }
}
