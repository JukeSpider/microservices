package com.juke.profileservice.feature.profile.presentation.dto

import com.juke.profileservice.core.data.entity.ProfileEntity
import com.juke.profileservice.core.data.entity.UserEntity
import java.time.LocalDate
import java.util.*

data class ProfileResponse(
    val id: UUID,
    val firstname: String,
    val lastname: String,
    val birthDate: LocalDate,
    val address: String,
    val jobPosition: UUID,
    val startDate: LocalDate,
    val phone: String,
    val telegramLink: String,
    val email: String,
) {
    constructor(profile: ProfileEntity, user: UserEntity) : this(
        id = profile.id!!,
        firstname = profile.firstname,
        lastname = profile.lastname,
        birthDate = profile.birthDate,
        address = profile.address,
        jobPosition = profile.jobPositionID,
        startDate = profile.startDate,
        phone = profile.phone,
        telegramLink = profile.telegramLink,
        email = user.email
    )
}