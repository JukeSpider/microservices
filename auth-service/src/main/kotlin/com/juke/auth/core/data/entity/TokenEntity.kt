package com.juke.auth.core.data.entity

import com.juke.auth.core.data.entity.enums.TokenTypeEnum
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.UUID

@Table(name = "tokens")
data class TokenEntity (

    @Id
    val id: UUID?,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),

    val userId: UUID,
    val token: String,
    val type: TokenTypeEnum,
) {
    constructor(userId: UUID, token: String, type: TokenTypeEnum) : this(id = null, userId = userId, token = token, type = type)
}