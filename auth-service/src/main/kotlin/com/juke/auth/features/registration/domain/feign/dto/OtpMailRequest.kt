package com.juke.auth.features.registration.domain.feign.dto

data class OtpMailRequest(
    val code: String,
    val email: String,
)
