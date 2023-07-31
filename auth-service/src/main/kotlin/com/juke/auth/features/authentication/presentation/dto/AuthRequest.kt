package com.juke.auth.features.authentication.presentation.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class AuthRequest(

    @field:NotNull
    @field:NotBlank
    val email: String?,

    @field:NotNull
    @field:NotBlank
    val password: String?,
)