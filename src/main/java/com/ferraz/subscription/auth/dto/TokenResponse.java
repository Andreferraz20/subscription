package com.ferraz.subscription.auth.dto;

public record TokenResponse(String accessToken, String refreshToken, long expiresIn) {}
