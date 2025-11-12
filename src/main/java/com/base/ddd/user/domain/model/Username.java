package com.base.ddd.user.domain.model;

import com.base.ddd.shared.domain.ValueObject;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Username Value Object with validation
 */
public class Username implements ValueObject {

    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]+$");

    private final String value;

    private Username(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Username không được để trống");
        }
        if (value.length() < 3 || value.length() > 50) {
            throw new IllegalArgumentException("Username phải từ 3-50 ký tự");
        }
        if (!USERNAME_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Username chỉ chứa chữ, số và dấu gạch dưới");
        }
        this.value = value.trim();
    }

    public static Username of(String value) {
        return new Username(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Username username = (Username) o;
        return Objects.equals(value, username.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}