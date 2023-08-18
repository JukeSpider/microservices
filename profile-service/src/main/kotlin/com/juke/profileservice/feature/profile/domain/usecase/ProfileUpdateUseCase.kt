package com.juke.profileservice.feature.profile.domain.usecase

import com.juke.profileservice.core.data.entity.ProfileEntity
import com.juke.profileservice.core.data.entity.enums.UserStatusEnum
import com.juke.profileservice.core.domain.behavior.ProfileBehavior
import com.juke.profileservice.core.domain.behavior.UserBehavior
import com.juke.profileservice.core.domain.failure.AuthenticationFailure
import com.juke.profileservice.core.domain.failure.EmailNotFoundFailure
import com.juke.profileservice.core.domain.model.Data
import com.juke.profileservice.core.domain.model.Data.Error
import com.juke.profileservice.core.domain.model.Data.Success
import com.juke.profileservice.core.domain.model.value
import com.juke.profileservice.core.domain.success.StatusResponse
import com.juke.profileservice.core.domain.usecase.UseCase
import com.juke.profileservice.core.domain.utils.JwtUtils
import com.juke.profileservice.feature.profile.domain.failure.ProfileNotAvailableFailure
import com.juke.profileservice.feature.profile.domain.success.ProfileStatusEnum.UPDATED
import com.juke.profileservice.feature.profile.domain.usecase.ProfileUpdateUseCase.UpdateParams
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime

@Component
class ProfileUpdateUseCase(
    private val userService: UserBehavior,
    private val profileService: ProfileBehavior,
    private val jwtUtils: JwtUtils,
) : UseCase<UpdateParams, StatusResponse> {

    data class UpdateParams(
        val firstname: String,
        val lastname: String,
        val birthDate: LocalDate?,
        val address: String?,
        val phone: String?,
        val telegramLink: String?,
        val jwt: String,
    )

    override suspend fun invoke(params: UpdateParams): Data<StatusResponse> {
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
        if (user.status != UserStatusEnum.ACTIVE) return Error(ProfileNotAvailableFailure())

        val profileData = profileService.findByUserId(user.id!!)
        if (profileData is Error) {
            return when (profileData.failure) {
                is ProfileNotAvailableFailure -> Error(ProfileNotAvailableFailure())
                else -> Error(profileData.failure)
            }
        }

        val profile = profileData.value!!
        val updatedProfile = getUpdatedProfile(profile, params)

        val updatedData = profileService.save(updatedProfile)
        if (updatedData is Error) return Error(updatedData.failure)

        return Success(StatusResponse(UPDATED.getMessage()))
    }

    private fun getUpdatedProfile(profile: ProfileEntity, params: UpdateParams): ProfileEntity {
        return profile.copy(
            updatedAt = LocalDateTime.now(),
            firstname = params.firstname,
            lastname = params.lastname,
            birthDate = params.birthDate ?: profile.birthDate,
            address = params.address ?: profile.address,
            phone = params.phone ?: profile.phone,
            telegramLink = params.telegramLink ?: profile.telegramLink
        )
    }
}