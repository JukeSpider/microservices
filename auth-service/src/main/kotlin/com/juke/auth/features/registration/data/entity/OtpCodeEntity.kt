package com.juke.auth.features.registration.data.entity

import com.juke.auth.features.registration.data.entity.enums.OtpStatusEnum
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.UUID

@Table(name = "otp_codes")
data class OtpCodeEntity(

    @Id
    val id: UUID? = null,
    val code: String,
    var status: OtpStatusEnum,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    var expiresAt: LocalDateTime,
    var updatedAt: LocalDateTime = LocalDateTime.now(),
    val userId: UUID
)