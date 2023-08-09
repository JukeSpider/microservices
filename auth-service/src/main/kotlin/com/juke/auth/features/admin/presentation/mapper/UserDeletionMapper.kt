package com.juke.auth.features.admin.presentation.mapper

import com.juke.auth.core.presentation.mapper.Mapper
import com.juke.auth.features.admin.domain.usecase.UserDeletionUseCase.UserDeletionParams
import com.juke.auth.features.admin.presentation.dto.UserDeletionRequest
import org.springframework.stereotype.Component

@Component
class UserDeletionMapper: Mapper<UserDeletionRequest, UserDeletionParams> {

    override fun convert(request: UserDeletionRequest): UserDeletionParams {
        return UserDeletionParams(email = request.email!!)
    }
}
