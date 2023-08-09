package com.juke.auth.features.authentication.domain.usecase

import com.juke.auth.core.data.entity.TokenEntity
import com.juke.auth.core.data.entity.enums.TokenTypeEnum.ACCESS
import com.juke.auth.core.data.entity.enums.TokenTypeEnum.REFRESH
import com.juke.auth.core.data.entity.enums.UserRoleEnum.ROLE_USER
import com.juke.auth.core.data.entity.enums.UserStatusEnum
import com.juke.auth.core.domain.behavior.PasswordBehavior
import com.juke.auth.core.domain.behavior.TokenBehavior
import com.juke.auth.core.domain.behavior.UserBehavior
import com.juke.auth.core.domain.model.Data
import com.juke.auth.core.domain.model.Data.Error
import com.juke.auth.core.domain.model.value
import com.juke.auth.core.domain.usecase.UseCase
import com.juke.auth.core.domain.utils.JwtUtils
import com.juke.auth.features.authentication.domain.failure.BadCredentialsFailure
import com.juke.auth.features.authentication.domain.failure.EmailNotFoundFailure
import com.juke.auth.features.authentication.domain.failure.PasswordNotFoundFailure
import com.juke.auth.features.authentication.domain.usecase.AuthUseCase.AuthParams
import com.juke.auth.features.authentication.presentation.dto.AuthResponse
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class AuthUseCase(
    private val userService: UserBehavior,
    private val passwordService: PasswordBehavior,
    private val tokenService: TokenBehavior,
    private val encoder: PasswordEncoder,
    private val jwtUtils: JwtUtils,
) : UseCase<AuthParams, AuthResponse> {

    data class AuthParams(
        val email: String,
        val password: String,
    )

    @Transactional
    override suspend fun invoke(params: AuthParams): Data<AuthResponse> {

        val userData = userService.findByEmail(params.email)
        if (userData is Error) {
            return when(userData.failure) {
                is EmailNotFoundFailure -> Error(BadCredentialsFailure())
                else -> Error(userData.failure)
            }
        }

        val user = userData.value!!

        if (user.status == UserStatusEnum.CREATED || user.status == UserStatusEnum.DELETED) {
            return Error(BadCredentialsFailure())
        }

        val passwordData = passwordService.findActiveUserPassword(user.id!!)
        if (passwordData is Error) {
            return when(passwordData.failure) {
                is PasswordNotFoundFailure -> Error(BadCredentialsFailure())
                else -> Error(passwordData.failure)
            }
        }

        val password = passwordData.value!!

        if (!encoder.matches(params.password, password.pwd)) {
            return Error(BadCredentialsFailure())
        }

        val access = jwtUtils.getToken(params.email, ROLE_USER, ACCESS)
        val refresh = jwtUtils.getToken(params.email, ROLE_USER, REFRESH)

        val accessEntity = TokenEntity(userId = user.id, token = access, type = ACCESS)
        val refreshEntity = TokenEntity(userId = user.id, token = refresh, type = REFRESH)

        val accessSaveData = tokenService.save(accessEntity)
        if (accessSaveData is Error) return Error(accessSaveData.failure)

        val refreshSaveData = tokenService.save(refreshEntity)
        if (refreshSaveData is Error) return Error(refreshSaveData.failure)

        return Data.Success(AuthResponse(access = access, refresh = refresh))
    }
}