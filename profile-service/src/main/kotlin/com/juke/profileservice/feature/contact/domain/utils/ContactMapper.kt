package com.juke.profileservice.feature.contact.domain.utils

import com.juke.profileservice.core.data.entity.ProfileEntity
import com.juke.profileservice.core.data.entity.UserEntity
import com.juke.profileservice.feature.contact.rest.dto.ContactResponse
import com.juke.profileservice.feature.contact.rest.dto.FullContactResponse
import org.springframework.stereotype.Component

@Component
class ContactMapper {

    fun convertToContact(profile: ProfileEntity): ContactResponse {
        return ContactResponse(
            id = profile.userId,
            firstname = profile.firstname,
            lastname = profile.lastname,
            jobPosition = profile.jobPositionID
        )
    }

    fun convertToFullContact(profile: ProfileEntity, user: UserEntity): FullContactResponse {
        return FullContactResponse(
            id = profile.userId,
            firstname = profile.firstname,
            lastname = profile.lastname,
            birthDate = profile.birthDate,
            jobPosition = profile.jobPositionID,
            phone = profile.phone,
            telegramLink = profile.telegramLink,
            email = user.email
        )
    }
}