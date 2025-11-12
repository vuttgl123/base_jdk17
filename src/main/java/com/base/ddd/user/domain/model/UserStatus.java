package com.base.ddd.user.domain.model;

/**
 * User Status Domain Enum
 */
public enum UserStatus {
    ACTIVE("Active", "Đang hoạt động"),
    INACTIVE("Inactive", "Không hoạt động"),
    BLOCKED("Blocked", "Bị khóa"),
    PENDING("Pending", "Chờ xác nhận");

    private final String code;
    private final String description;

    UserStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static UserStatus fromCode(String code) {
        for (UserStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status code: " + code);
    }
}