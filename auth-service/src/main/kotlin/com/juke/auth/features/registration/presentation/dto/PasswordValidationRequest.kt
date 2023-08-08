package com.juke.auth.features.registration.presentation.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class PasswordValidationRequest(

    @NotNull
    @Email
    val email: String?,

    @NotNull
    @NotBlank
    val code: String?,

    @NotNull
    @NotBlank
    val password: String?,

    @NotNull
    @NotBlank
    val flow: String?,
)
