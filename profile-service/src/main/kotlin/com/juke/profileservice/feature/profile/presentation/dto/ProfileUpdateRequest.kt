package com.juke.profileservice.feature.profile.presentation.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDate

data class ProfileUpdateRequest(

    @NotNull
    @NotBlank
    val firstname: String?,

    @NotNull
    @NotBlank
    val lastname: String?,

    val birthDate: LocalDate?,
    val address: String?,
    val phone: String?,
    val telegramLink: String?,

    @JsonIgnore
    val bearerToken: String? = null,
)