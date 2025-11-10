package com.base.model.response.auth;

public record TokenResponse(String tokenType, String accessToken, long expiresInSeconds,
                            String refreshToken) { }
