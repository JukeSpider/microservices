package com.juke.auth.features.admin.presentation.mapper

import com.juke.auth.core.presentation.mapper.Mapper
import com.juke.auth.features.admin.domain.usecase.UserCreationUseCase.UserCreationParams
import com.juke.auth.features.admin.presentation.dto.UserCreationRequest
import org.springframework.stereotype.Component

@Component
class UserCreationMapper : Mapper<UserCreationRequest, UserCreationParams> {

    override fun convert(request: UserCreationRequest): UserCreationParams {
        return UserCreationParams(email = request.email!!)
    }
}