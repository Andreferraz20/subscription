package com.ferraz.subscription.auth.dto;

import java.time.LocalDate;

public record SignupRequest(String email, String password, String firstName, String lastName, String document, String phone, LocalDate birthDate) {}
