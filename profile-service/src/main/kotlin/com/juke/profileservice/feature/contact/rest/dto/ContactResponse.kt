package com.juke.profileservice.feature.contact.rest.dto

import java.util.*

data class ContactResponse(
    val id: UUID,
    val firstname: String,
    val lastname: String,
    val jobPosition: UUID
)