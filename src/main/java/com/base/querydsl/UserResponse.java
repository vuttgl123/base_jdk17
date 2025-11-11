package com.base.querydsl;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserResponse extends BaseResponse {
    private String username;
    private String email;
    private String fullName;
    private String phoneNumber;
    private String status;
    private Integer age;
    private String address;
    private String avatarUrl;
}
