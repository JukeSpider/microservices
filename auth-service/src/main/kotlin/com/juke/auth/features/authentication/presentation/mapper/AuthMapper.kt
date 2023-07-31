package com.juke.auth.features.authentication.presentation.mapper

import com.juke.auth.core.presentation.mapper.Mapper
import com.juke.auth.features.authentication.domain.usecase.AuthUseCase.AuthParams
import com.juke.auth.features.authentication.presentation.dto.AuthRequest
import org.springframework.stereotype.Component

@Component
class AuthMapper : Mapper<AuthRequest, AuthParams> {
    override fun convert(request: AuthRequest): AuthParams {
        return AuthParams(
            email = request.email!!,
            password = request.password!!
        )
    }
}