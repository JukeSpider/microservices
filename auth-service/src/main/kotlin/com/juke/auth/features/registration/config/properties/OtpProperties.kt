package com.juke.auth.features.registration.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("otp.expiration")
data class OtpProperties(
    val pending: Long,
    val confirmed: Long,
)