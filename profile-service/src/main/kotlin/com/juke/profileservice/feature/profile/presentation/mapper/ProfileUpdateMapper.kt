package com.juke.profileservice.feature.profile.presentation.mapper

import com.juke.profileservice.core.presentation.mapper.Mapper
import com.juke.profileservice.feature.profile.domain.usecase.ProfileUpdateUseCase.UpdateParams
import com.juke.profileservice.feature.profile.presentation.dto.ProfileUpdateRequest
import org.springframework.stereotype.Component

@Component
class ProfileUpdateMapper : Mapper<ProfileUpdateRequest, UpdateParams> {

    override fun convert(request: ProfileUpdateRequest): UpdateParams {
        return UpdateParams(
            firstname = request.firstname!!,
            lastname = request.lastname!!,
            birthDate = request.birthDate,
            address = request.address,
            phone = request.phone,
            telegramLink = request.telegramLink,
            jwt = request.bearerToken!!.substring(7)
        )
    }
}