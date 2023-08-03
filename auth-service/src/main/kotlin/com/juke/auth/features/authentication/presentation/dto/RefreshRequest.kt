package com.juke.auth.features.authentication.presentation.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class RefreshRequest (

    @NotNull
    @NotBlank
    val refresh: String?
)
