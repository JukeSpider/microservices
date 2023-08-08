package com.juke.auth.features.registration.domain.usecase

import com.juke.auth.core.data.entity.enums.UserStatusEnum.CREATED
import com.juke.auth.core.domain.behavior.UserBehavior
import com.juke.auth.core.domain.model.Data
import com.juke.auth.core.domain.model.Data.Error
import com.juke.auth.core.domain.model.Data.Success
import com.juke.auth.core.domain.model.value
import com.juke.auth.core.domain.success.StatusResponse
import com.juke.auth.core.domain.usecase.UseCase
import com.juke.auth.features.authentication.domain.failure.EmailNotFoundFailure
import com.juke.auth.features.registration.data.entity.enums.OtpStatusEnum.PENDING
import com.juke.auth.features.registration.domain.behavior.OtpCodeBehavior
import com.juke.auth.features.registration.domain.failure.RegistrationUnavailableFailure
import com.juke.auth.features.registration.domain.usecase.EmailValidationUseCase.EmailParams
import com.juke.auth.features.registration.domain.utils.OtpUtils
import com.juke.auth.features.registration.presentation.dto.enums.RegistrationStatusEnum.EMAIL_VALIDATED
import org.springframework.stereotype.Component

@Component
class EmailValidationUseCase(
    private val userService: UserBehavior,
    private val otpService: OtpCodeBehavior,
    private val otpUtils: OtpUtils,
) : UseCase<EmailParams, StatusResponse> {

    data class EmailParams(
        val email: String
    )

    override suspend fun invoke(params: EmailParams): Data<StatusResponse> {
        val userData = userService.findByEmail(params.email)
        if (userData is Error) {
            return when (userData.failure) {
                is EmailNotFoundFailure -> Error(RegistrationUnavailableFailure())
                else -> Error(userData.failure)
            }
        }

        val user = userData.value!!
        if (user.status != CREATED) return Error(RegistrationUnavailableFailure())

        val revokedData = otpService.revokeAllUserOtpCodes(user.id!!)
        if (revokedData is Error) return Error(revokedData.failure)

        val otpCode = otpUtils.generateOtpCodeEntity(status = PENDING, userId = user.id)

        val otpCodeData = otpService.save(otpCode)
        if (otpCodeData is Error) return Error(otpCodeData.failure)

        return Success(StatusResponse(EMAIL_VALIDATED.getMessage()))
    }
}