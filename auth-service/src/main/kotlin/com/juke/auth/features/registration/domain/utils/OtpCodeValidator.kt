package com.juke.auth.features.registration.domain.utils

import com.juke.auth.core.domain.model.Data
import com.juke.auth.core.domain.model.Data.Error
import com.juke.auth.core.domain.model.Data.Success
import com.juke.auth.core.domain.model.value
import com.juke.auth.features.registration.data.entity.OtpCodeEntity
import com.juke.auth.features.registration.data.entity.enums.FlowTypeEnum
import com.juke.auth.features.registration.data.entity.enums.FlowTypeEnum.REGISTRATION
import com.juke.auth.features.registration.data.entity.enums.FlowTypeEnum.UNDEFINED
import com.juke.auth.features.registration.data.entity.enums.OtpStatusEnum
import com.juke.auth.features.registration.domain.behavior.OtpCodeBehavior
import com.juke.auth.features.registration.domain.failure.OtpNotFoundFailure
import com.juke.auth.features.registration.domain.failure.PasswordResetUnavailableFailure
import com.juke.auth.features.registration.domain.failure.RegistrationUnavailableFailure
import com.juke.auth.features.registration.domain.failure.UndefinedFlowFailure
import org.springframework.stereotype.Component
import java.util.*

@Component
class OtpCodeValidator(
    private val otpService: OtpCodeBehavior,
    private val otpUtils: OtpUtils,
) {

    suspend fun validateOtpCode(
        code: String, userId: UUID, status: OtpStatusEnum, flow: FlowTypeEnum
    ) : Data<OtpCodeEntity> {
        if (flow == UNDEFINED) return Error(UndefinedFlowFailure())

        val otpCodeData = otpService.findByCodeUserFlow(code, userId, flow)

        val failure =
            if (flow == REGISTRATION)   RegistrationUnavailableFailure()
            else                        PasswordResetUnavailableFailure()

        if (otpCodeData is Error) {
            return when (otpCodeData.failure) {
                is OtpNotFoundFailure -> Error(failure)
                else -> Error(otpCodeData.failure)
            }
        }

        val otpCode = otpCodeData.value!!
        if (otpCode.status != status) return Error(failure)
        if (otpUtils.isExpired(otpCode)) return Error(failure)

        return Success(otpCode)
    }
}