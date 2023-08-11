package com.juke.notification.core.data.entity

import com.juke.notification.core.data.entity.enums.PasswordStatusEnum
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.*

@Table(name = "passwords")
data class PasswordEntity (

    @Id
    val id: UUID? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),

    val userId: UUID,
    val pwd: String,
    val status: PasswordStatusEnum,
)