package com.juke.auth.features.authentication.presentation.dto

data class AuthResponse(
    val access: String,
    val refresh: String,
)