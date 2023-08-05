package com.juke.auth.features.registration.presentation.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull

data class EmailValidationRequest(

    @NotNull
    @Email
    val email: String
)
