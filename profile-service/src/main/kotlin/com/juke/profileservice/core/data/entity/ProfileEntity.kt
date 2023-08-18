package com.juke.profileservice.core.data.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@Table(name = "profiles")
data class ProfileEntity(

    @Id
    val id: UUID? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),

    val firstname: String,
    val lastname: String,
    val birthDate: LocalDate,
    val address: String,
    val startDate: LocalDate,
    val phone: String,
    val telegramLink: String,

    val userId: UUID,
    val jobPositionID: UUID
)