package br.com.compass.ecommercechallenge.model;

public enum UserTypeEnum {
    ADMIN("Administrator"),
    DEFAULT("Default");

    public final String label;

    UserTypeEnum(String label) {
        this.label = label;
    }

    public enum Values {
        ADMIN(1),
        DEFAULT(2);

        int roleId;

        Values(int roleId) {
            this.roleId = roleId;
        }
    }
}
