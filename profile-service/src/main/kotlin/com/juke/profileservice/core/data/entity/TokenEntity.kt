package com.juke.profileservice.core.data.entity

import com.juke.profileservice.core.data.entity.enums.TokenTypeEnum
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.*

@Table(name = "tokens")
data class TokenEntity (

    @Id
    val id: UUID? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),

    val userId: UUID,
    val token: String,
    val type: TokenTypeEnum,
)