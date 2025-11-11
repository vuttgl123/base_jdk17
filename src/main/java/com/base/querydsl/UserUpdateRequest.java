package com.base.querydsl;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserUpdateRequest {

    @Email(message = "Email không đúng định dạng")
    @Size(max = 100)
    private String email;

    @Size(max = 100)
    private String fullName;

    @Pattern(regexp = "^[0-9+\\-\\s()]*$", message = "Số điện thoại không hợp lệ")
    @Size(max = 20)
    private String phoneNumber;

    @Min(value = 1)
    @Max(value = 150)
    private Integer age;

    private String address;
    private String avatarUrl;
    private String status;
}
