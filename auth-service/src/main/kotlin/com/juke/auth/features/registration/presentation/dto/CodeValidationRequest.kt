package com.juke.auth.features.registration.presentation.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class CodeValidationRequest(

    @NotNull
    @NotBlank
    val email: String?,

    @NotNull
    @NotBlank
    val code: String?
)
