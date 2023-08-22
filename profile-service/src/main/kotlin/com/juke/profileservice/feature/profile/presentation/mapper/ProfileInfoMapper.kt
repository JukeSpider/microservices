package com.juke.profileservice.feature.profile.presentation.mapper

import com.juke.profileservice.core.presentation.mapper.Mapper
import com.juke.profileservice.feature.profile.domain.usecase.ProfileInfoUseCase.ProfileInfoParams
import org.springframework.stereotype.Component

@Component
class ProfileInfoMapper : Mapper<String, ProfileInfoParams> {

    override fun convert(request: String): ProfileInfoParams {
        return ProfileInfoParams(jwt = request.substring(7))
    }
}