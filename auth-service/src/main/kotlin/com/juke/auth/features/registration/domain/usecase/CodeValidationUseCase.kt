package com.juke.auth.features.registration.domain.usecase

import com.juke.auth.core.domain.model.Data
import com.juke.auth.core.domain.model.Data.Error
import com.juke.auth.core.domain.model.Data.Success
import com.juke.auth.core.domain.model.value
import com.juke.auth.core.domain.success.StatusResponse
import com.juke.auth.core.domain.usecase.UseCase
import com.juke.auth.features.registration.config.properties.OtpProperties
import com.juke.auth.features.registration.data.entity.enums.FlowTypeEnum
import com.juke.auth.features.registration.data.entity.enums.OtpStatusEnum.CONFIRMED
import com.juke.auth.features.registration.data.entity.enums.OtpStatusEnum.PENDING
import com.juke.auth.features.registration.domain.behavior.OtpCodeBehavior
import com.juke.auth.features.registration.domain.usecase.CodeValidationUseCase.CodeParams
import com.juke.auth.features.registration.domain.utils.EmailValidator
import com.juke.auth.features.registration.domain.utils.OtpCodeValidator
import com.juke.auth.features.registration.presentation.dto.enums.RegistrationStatusEnum.OTP_CODE_VALIDATED
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class CodeValidationUseCase(
    private val otpService: OtpCodeBehavior,
    private val otpProperties: OtpProperties,
    private val emailValidator: EmailValidator,
    private val otpCodeValidator: OtpCodeValidator,
) : UseCase<CodeParams, StatusResponse> {

    data class CodeParams(
        val email: String,
        val code: String,
        val flow: FlowTypeEnum,
    )

    override suspend fun invoke(params: CodeParams): Data<StatusResponse> {
        val userData = emailValidator.validateEmail(params.email, params.flow)
        if (userData is Error) return Error(userData.failure)
        val user = userData.value!!

        val otpCodeData = otpCodeValidator.validateOtpCode(
            code = params.code,
            userId = user.id!!,
            status = PENDING,
            flow = params.flow,
        )
        if (otpCodeData is Error) return Error(otpCodeData.failure)
        val otpCode = otpCodeData.value!!

        val updatedOtp = otpCode.copy(
            status = CONFIRMED,
            updatedAt = LocalDateTime.now(),
            expiresAt = LocalDateTime.now().plusMinutes(otpProperties.confirmed)
        )

        val savedData = otpService.save(updatedOtp)
        if (savedData is Error) return Error(savedData.failure)

        return Success(StatusResponse(OTP_CODE_VALIDATED.getMessage()))
    }
}
