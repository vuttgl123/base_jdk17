package com.base.ddd.user.domain.model;

import com.base.ddd.shared.domain.ValueObject;

import java.util.Objects;

/**
 * Password Value Object
 * Note: Only stores hashed password, never plain password
 */
public class Password implements ValueObject {

    private final String hashedValue;

    private Password(String hashedValue) {
        if (hashedValue == null || hashedValue.isBlank()) {
            throw new IllegalArgumentException("Password hash không được để trống");
        }
        this.hashedValue = hashedValue;
    }

    /**
     * Create from already hashed password
     */
    public static Password fromHash(String hashedValue) {
        return new Password(hashedValue);
    }

    /**
     * Validate plain password before hashing
     */
    public static void validatePlainPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.isBlank()) {
            throw new IllegalArgumentException("Password không được để trống");
        }
        if (plainPassword.length() < 6) {
            throw new IllegalArgumentException("Password tối thiểu 6 ký tự");
        }
        if (plainPassword.length() > 100) {
            throw new IllegalArgumentException("Password không được quá 100 ký tự");
        }

        // Optional: Add more validation rules
        // - Must contain uppercase
        // - Must contain lowercase
        // - Must contain digit
        // - Must contain special character
    }

    public String getValue() {
        return hashedValue;
    }

    public String getHashedValue() {
        return hashedValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Password password = (Password) o;
        return Objects.equals(hashedValue, password.hashedValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hashedValue);
    }

    @Override
    public String toString() {
        return "******"; // Never expose password
    }
}