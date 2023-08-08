package com.juke.auth.features.registration.data.entity

import com.juke.auth.features.registration.data.entity.enums.OtpStatusEnum
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.*

@Table(name = "otp_codes")
data class OtpCodeEntity(

    @Id
    val id: UUID? = null,
    val code: String,
    val status: OtpStatusEnum,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val expiresAt: LocalDateTime,
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val userId: UUID
)