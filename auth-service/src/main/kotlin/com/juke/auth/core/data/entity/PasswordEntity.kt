package com.juke.auth.core.data.entity

import com.juke.auth.core.data.entity.enums.PasswordStatusEnum
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.UUID

@Table(name = "passwords")
data class PasswordEntity (

    @Id
    val id: UUID = UUID.randomUUID(),
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val modifiedAt: LocalDateTime = LocalDateTime.now(),

    val userId: UUID,
    val pwd: String,
    val status: PasswordStatusEnum,
)