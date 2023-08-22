package com.juke.profileservice.feature.contact.rest.dto

import java.time.LocalDate
import java.util.*

data class FullContactResponse(
    val id: UUID,
    val firstname: String,
    val lastname: String,
    val birthDate: LocalDate,
    val jobPosition: UUID,
    val phone: String,
    val telegramLink: String,
    val email: String,
)