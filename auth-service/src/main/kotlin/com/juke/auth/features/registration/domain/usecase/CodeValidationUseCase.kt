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
import com.juke.auth.features.registration.data.entity.enums.OtpStatusEnum.CONFIRMED
import com.juke.auth.features.registration.data.entity.enums.OtpStatusEnum.PENDING
import com.juke.auth.features.registration.domain.behavior.OtpCodeBehavior
import com.juke.auth.features.registration.domain.failure.OtpNotFoundFailure
import com.juke.auth.features.registration.domain.failure.RegistrationUnavailableFailure
import com.juke.auth.features.registration.domain.usecase.CodeValidationUseCase.CodeParams
import com.juke.auth.features.registration.domain.utils.OtpUtils
import com.juke.auth.features.registration.presentation.dto.enums.RegistrationStatusEnum.OTP_CODE_VALIDATED
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class CodeValidationUseCase(
    private val otpService: OtpCodeBehavior,
    private val userService: UserBehavior,
    private val otpUtils: OtpUtils,
) : UseCase<CodeParams, StatusResponse> {

    data class CodeParams(
        val email: String,
        val code: String
    )

    override suspend fun invoke(params: CodeParams): Data<StatusResponse> {
        val userData = userService.findByEmail(params.email)
        if (userData is Error) {
            return when (userData.failure) {
                is EmailNotFoundFailure -> Error(RegistrationUnavailableFailure())
                else -> Error(userData.failure)
            }
        }

        val user = userData.value!!
        if (user.status != CREATED) return Error(RegistrationUnavailableFailure())

        val otpCodeData = otpService.findByCodeAndUser(code = params.code, userId = user.id!!)
        if (otpCodeData is Error) {
            return when (otpCodeData.failure) {
                is OtpNotFoundFailure -> Error(RegistrationUnavailableFailure())
                else -> Error(otpCodeData.failure)
            }
        }

        val otpCode = otpCodeData.value!!
        if (otpCode.status != PENDING) return Error(RegistrationUnavailableFailure())
        if (otpUtils.isExpired(otpCode)) return Error(RegistrationUnavailableFailure())

        otpCode.updatedAt = LocalDateTime.now()
        otpCode.expiresAt = LocalDateTime.now().plusMinutes(15)
        otpCode.status = CONFIRMED

        val savedData = otpService.save(otpCode)
        if (savedData is Error) return Error(savedData.failure)

        return Success(StatusResponse(OTP_CODE_VALIDATED.getMessage()))
    }
}
