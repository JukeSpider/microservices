package com.juke.auth.features.authentication.domain.usecase

import com.juke.auth.core.data.entity.TokenEntity
import com.juke.auth.core.data.entity.enums.TokenTypeEnum.ACCESS
import com.juke.auth.core.data.entity.enums.TokenTypeEnum.REFRESH
import com.juke.auth.core.data.entity.enums.UserStatusEnum.CREATED
import com.juke.auth.core.data.entity.enums.UserStatusEnum.DELETED
import com.juke.auth.core.domain.behavior.TokenBehavior
import com.juke.auth.core.domain.behavior.UserBehavior
import com.juke.auth.core.domain.failure.InvalidTokenFailure
import com.juke.auth.core.domain.model.Data
import com.juke.auth.core.domain.model.Data.Error
import com.juke.auth.core.domain.model.failure
import com.juke.auth.core.domain.model.value
import com.juke.auth.core.domain.usecase.UseCase
import com.juke.auth.core.domain.utils.JwtUtils
import com.juke.auth.features.authentication.domain.failure.EmailNotFoundFailure
import com.juke.auth.features.authentication.domain.usecase.RefreshUseCase.RefreshParams
import com.juke.auth.features.authentication.presentation.dto.RefreshResponse
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class RefreshUseCase(
    private val userService: UserBehavior,
    private val tokenService: TokenBehavior,
    private val jwtUtils: JwtUtils,
) : UseCase<RefreshParams, RefreshResponse> {

    data class RefreshParams(
        val refresh: String,
    )

    @Transactional
    override suspend fun invoke(params: RefreshParams): Data<RefreshResponse> {

        val credentialsData = jwtUtils.getCredentials(params.refresh)
        val credentials = credentialsData.value ?: return Error(credentialsData.failure!!)

        val userData = userService.findByEmail(credentials.email)
        if (userData is Error && userData.failure is EmailNotFoundFailure) {
            return Error(InvalidTokenFailure())
        }

        val user = userData.value ?: return Error(userData.failure!!)

        if (user.status == CREATED || user.status == DELETED) {
            return Error(InvalidTokenFailure())
        }

        val tokenData = tokenService.findByToken(params.refresh, REFRESH)
        val token = tokenData.value ?: return Error(tokenData.failure!!)

        val deleteData = tokenService.deleteById(token.id!!)
        if (deleteData is Error) return Error(deleteData.failure)

        val access = jwtUtils.getToken(user.email, user.role, ACCESS)
        val refresh = jwtUtils.getToken(user.email, user.role, REFRESH)

        val accessEntity = TokenEntity(userId = user.id!!, token = access, type = ACCESS)
        val refreshEntity = TokenEntity(userId = user.id, token = access, type = REFRESH)

        val saveAccessData = tokenService.save(accessEntity)
        if (saveAccessData is Error) return Error(saveAccessData.failure)

        val saveRefreshData = tokenService.save(refreshEntity)
        if (saveRefreshData is Error) return Error(saveRefreshData.failure)

        return Data.Success(RefreshResponse(access = access, refresh = refresh))
    }
}