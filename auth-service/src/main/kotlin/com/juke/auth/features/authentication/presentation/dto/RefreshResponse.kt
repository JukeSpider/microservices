package com.juke.auth.features.authentication.presentation.dto

data class RefreshResponse (
    val access: String,
    val refresh: String,
)