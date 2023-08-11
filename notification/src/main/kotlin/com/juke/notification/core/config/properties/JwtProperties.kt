package com.juke.notification.core.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("crypto.jwt")
data class JwtProperties(
    val secret: String,
    val access: Long,
    val refresh: Long,
)