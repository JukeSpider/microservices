package com.juke.auth.features.admin.presentation.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull

data class UserCreationRequest(

    @NotNull
    @Email
    val email: String?,
)