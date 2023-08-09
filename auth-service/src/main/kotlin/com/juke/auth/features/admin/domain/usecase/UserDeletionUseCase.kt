package com.juke.auth.features.admin.domain.usecase

import com.juke.auth.core.data.entity.enums.UserStatusEnum
import com.juke.auth.core.domain.behavior.PasswordBehavior
import com.juke.auth.core.domain.behavior.TokenBehavior
import com.juke.auth.core.domain.behavior.UserBehavior
import com.juke.auth.core.domain.model.Data
import com.juke.auth.core.domain.model.Data.*
import com.juke.auth.core.domain.model.value
import com.juke.auth.core.domain.success.StatusResponse
import com.juke.auth.core.domain.usecase.UseCase
import com.juke.auth.features.admin.domain.success.StatusEnum.DELETED
import com.juke.auth.features.admin.domain.usecase.UserDeletionUseCase.UserDeletionParams
import com.juke.auth.features.registration.domain.behavior.OtpCodeBehavior
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class UserDeletionUseCase(
    private val userService: UserBehavior,
    private val passwordService: PasswordBehavior,
    private val otpCodeService: OtpCodeBehavior,
    private val tokenService: TokenBehavior,
): UseCase<UserDeletionParams, StatusResponse> {

    data class UserDeletionParams(
        val email: String
    )

    override suspend fun invoke(params: UserDeletionParams): Data<StatusResponse> {
        val userData = userService.findByEmail(params.email)
        if (userData is Error) return Error(userData.failure)

        val user = userData.value!!

        val userToDelete = user.copy(
            updatedAt = LocalDateTime.now(),
            status = UserStatusEnum.DELETED
        )
        val deleteUserData = userService.save(userToDelete)
        if (deleteUserData is Error) return Error(deleteUserData.failure)

        val passwordData = passwordService.revokeAllUserPasswords(userId = user.id!!)
        if (passwordData is Error) return Error(passwordData.failure)

        val otpData = otpCodeService.revokeAllUserOtpCodes(userId = user.id)
        if (otpData is Error) return Error(otpData.failure)

        val tokensData = tokenService.deleteAllUserTokens(userId = user.id)
        if (tokensData is Error) return Error(tokensData.failure)

        return Success(StatusResponse(DELETED.getMessage()))
    }
}
