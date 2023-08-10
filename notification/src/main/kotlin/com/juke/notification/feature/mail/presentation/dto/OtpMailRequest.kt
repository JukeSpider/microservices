package com.juke.notification.feature.mail.presentation.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class OtpMailRequest(

    @NotNull
    @NotBlank
    val code: String?,

    @Email
    @NotNull
    val email: String?,
)
