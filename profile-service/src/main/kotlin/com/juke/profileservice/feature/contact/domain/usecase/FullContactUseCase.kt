package com.juke.profileservice.feature.contact.domain.usecase

import com.juke.profileservice.core.domain.behavior.ProfileBehavior
import com.juke.profileservice.core.domain.behavior.UserBehavior
import com.juke.profileservice.core.domain.failure.ProfileNotFoundFailure
import com.juke.profileservice.core.domain.model.Data
import com.juke.profileservice.core.domain.model.Data.Error
import com.juke.profileservice.core.domain.model.Data.Success
import com.juke.profileservice.core.domain.model.value
import com.juke.profileservice.core.domain.usecase.UseCase
import com.juke.profileservice.feature.contact.domain.failure.ContactNotFoundFailure
import com.juke.profileservice.feature.contact.domain.usecase.FullContactUseCase.FullContactParams
import com.juke.profileservice.feature.contact.domain.utils.ContactMapper
import com.juke.profileservice.feature.contact.rest.dto.FullContactResponse
import org.springframework.stereotype.Component
import java.util.*

@Component
class FullContactUseCase(
    private val profileService: ProfileBehavior,
    private val userService: UserBehavior,
    private val contactMapper: ContactMapper,
) : UseCase<FullContactParams, FullContactResponse> {

    data class FullContactParams(
        val userId: UUID
    )

    override suspend fun invoke(params: FullContactParams): Data<FullContactResponse> {
        val profileData = profileService.findByUserId(params.userId)
        if (profileData is Error) {
            return when (profileData.failure) {
                is ProfileNotFoundFailure -> Error(ContactNotFoundFailure())
                else -> Error(profileData.failure)
            }
        }

        val profile = profileData.value!!

        val userData = userService.findById(profile.userId)
        if (userData is Error) return Error(userData.failure)

        val user = userData.value!!

        val fullContact = contactMapper.convertToFullContact(profile, user)
        return Success(fullContact)
    }
}