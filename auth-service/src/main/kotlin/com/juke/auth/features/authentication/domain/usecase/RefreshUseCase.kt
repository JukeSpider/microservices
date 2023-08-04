package com.juke.auth.features.authentication.domain.usecase

import com.juke.auth.core.data.entity.TokenEntity
import com.juke.auth.core.data.entity.enums.TokenTypeEnum.ACCESS
import com.juke.auth.core.data.entity.enums.TokenTypeEnum.REFRESH
import com.juke.auth.core.data.entity.enums.UserRoleEnum
import com.juke.auth.core.data.entity.enums.UserStatusEnum
import com.juke.auth.core.domain.behavior.TokenBehavior
import com.juke.auth.core.domain.behavior.UserBehavior
import com.juke.auth.core.domain.failure.InvalidTokenFailure
import com.juke.auth.core.domain.model.Data
import com.juke.auth.core.domain.model.Data.Error
import com.juke.auth.core.domain.model.Data.Success
import com.juke.auth.core.domain.model.value
import com.juke.auth.core.domain.usecase.UseCase
import com.juke.auth.core.domain.utils.JwtUtils
import com.juke.auth.features.authentication.domain.failure.EmailNotFoundFailure
import com.juke.auth.features.authentication.presentation.dto.RefreshResponse
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class RefreshUseCase(
    private val userService: UserBehavior,
    private val tokenService: TokenBehavior,
    private val jwtUtils: JwtUtils,
) : UseCase<RefreshUseCase.RefreshParams, RefreshResponse> {

    data class RefreshParams (
        val refresh: String,
    )

    @Transactional
    override suspend fun invoke(params: RefreshParams): Data<RefreshResponse> {

        val tokenData = tokenService.findByToken(params.refresh)
        if (tokenData is Error) return Error(tokenData.failure)

        val token = tokenData.value!!

        if (token.type != REFRESH) {
            return Error(InvalidTokenFailure())
        }

        val tokenCredentialsData = jwtUtils.getCredentials(params.refresh)

        if (tokenCredentialsData is Error) {
            return Error(tokenCredentialsData.failure)
        }

        val tokenCredentials = tokenCredentialsData.value!!

        val userData = userService.findByEmail(tokenCredentials.email)

        if (userData is Error) {
            return when (userData.failure) {
                is EmailNotFoundFailure -> Error(InvalidTokenFailure())
                else -> Error(userData.failure)
            }
        }

        val user = userData.value!!

        if (user.status == UserStatusEnum.CREATED || user.status == UserStatusEnum.DELETED) {
            return Error(InvalidTokenFailure())
        }

        val access = jwtUtils.getToken(tokenCredentials.email, UserRoleEnum.ROLE_USER, ACCESS)
        val refresh = jwtUtils.getToken(tokenCredentials.email, UserRoleEnum.ROLE_USER, REFRESH)

        val accessEntity = TokenEntity(userId = user.id, token = access, type = ACCESS)
        val refreshEntity = TokenEntity(userId = user.id, token = refresh, type = REFRESH)

        val accessSaveData = tokenService.save(accessEntity)
        if (accessSaveData is Error) return Error(accessSaveData.failure)

        val refreshSaveData = tokenService.save(refreshEntity)
        if (refreshSaveData is Error) return Error(refreshSaveData.failure)

        return Success(RefreshResponse(access = access, refresh = refresh))
    }
}