package com.juke.profileservice.feature.profile.domain.usecase

import com.juke.profileservice.core.data.entity.enums.UserStatusEnum.ACTIVE
import com.juke.profileservice.core.domain.behavior.ProfileBehavior
import com.juke.profileservice.core.domain.behavior.UserBehavior
import com.juke.profileservice.core.domain.failure.AuthenticationFailure
import com.juke.profileservice.core.domain.failure.EmailNotFoundFailure
import com.juke.profileservice.core.domain.model.Data
import com.juke.profileservice.core.domain.model.Data.Error
import com.juke.profileservice.core.domain.model.Data.Success
import com.juke.profileservice.core.domain.model.value
import com.juke.profileservice.core.domain.usecase.UseCase
import com.juke.profileservice.core.domain.utils.JwtUtils
import com.juke.profileservice.feature.profile.domain.failure.ProfileNotAvailableFailure
import com.juke.profileservice.feature.profile.domain.usecase.ProfileInfoUseCase.ProfileInfoParams
import com.juke.profileservice.feature.profile.presentation.dto.ProfileResponse
import org.springframework.stereotype.Component

@Component
class ProfileInfoUseCase(
    private val jwtUtils: JwtUtils,
    private val userService: UserBehavior,
    private val profileService: ProfileBehavior,
) : UseCase<ProfileInfoParams, ProfileResponse> {

    data class ProfileInfoParams(
        val jwt: String,
    )

    override suspend fun invoke(params: ProfileInfoParams): Data<ProfileResponse> {
        val tokenData = jwtUtils.getCredentials(params.jwt)
        if (tokenData is Error) return Error(tokenData.failure)

        val credentials = tokenData.value!!
        val userData = userService.findByEmail(credentials.email)
        if (userData is Error) {
            return when (userData.failure) {
                is EmailNotFoundFailure -> Error(AuthenticationFailure())
                else -> Error(userData.failure)
            }
        }

        val user = userData.value!!
        if (user.status != ACTIVE) return Error(ProfileNotAvailableFailure())

        val profileData = profileService.findByUserId(user.id!!)
        if (profileData is Error) {
            return when (profileData.failure) {
                is ProfileNotAvailableFailure -> Error(ProfileNotAvailableFailure())
                else -> Error(profileData.failure)
            }
        }

        val profile = profileData.value!!
        val response = ProfileResponse(profile, user)
        return Success(response)
    }
}