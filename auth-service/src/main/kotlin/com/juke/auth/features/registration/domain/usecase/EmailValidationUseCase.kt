package com.juke.auth.features.registration.domain.usecase

import com.juke.auth.core.domain.model.Data
import com.juke.auth.core.domain.model.Data.Error
import com.juke.auth.core.domain.model.Data.Success
import com.juke.auth.core.domain.model.value
import com.juke.auth.core.domain.success.StatusResponse
import com.juke.auth.core.domain.usecase.UseCase
import com.juke.auth.features.registration.data.entity.enums.FlowTypeEnum
import com.juke.auth.features.registration.data.entity.enums.OtpStatusEnum.PENDING
import com.juke.auth.features.registration.domain.behavior.OtpCodeBehavior
import com.juke.auth.features.registration.domain.feign.dto.OtpMailRequest
import com.juke.auth.features.registration.domain.feign.wrapper.NotificationClientWrapper
import com.juke.auth.features.registration.domain.rabbitmq.NotificationProducer
import com.juke.auth.features.registration.domain.usecase.EmailValidationUseCase.EmailParams
import com.juke.auth.features.registration.domain.utils.EmailValidator
import com.juke.auth.features.registration.domain.utils.OtpUtils
import com.juke.auth.features.registration.presentation.dto.enums.RegistrationStatusEnum.EMAIL_VALIDATED
import org.springframework.stereotype.Component

@Component
class EmailValidationUseCase(
    private val otpService: OtpCodeBehavior,
    private val otpUtils: OtpUtils,
    private val emailValidator: EmailValidator,
    private val notificationClient: NotificationClientWrapper,
    private val notificationProducer: NotificationProducer,
) : UseCase<EmailParams, StatusResponse> {

    data class EmailParams(
        val email: String,
        val flow: FlowTypeEnum,
    )

    override suspend fun invoke(params: EmailParams): Data<StatusResponse> {

        val userData = emailValidator.validateEmail(params.email, params.flow)
        if (userData is Error) return Error(userData.failure)

        val user = userData.value!!

        val revokedData = otpService.revokeAllUserOtpCodes(user.id!!)
        if (revokedData is Error) return Error(revokedData.failure)

        val otpCode = otpUtils.generateOtpCodeEntity(PENDING, user.id, params.flow)

        val otpCodeData = otpService.save(otpCode)
        if (otpCodeData is Error) return Error(otpCodeData.failure)

        val otpMailRequest = OtpMailRequest(code = otpCode.code, email = user.email)

//        val mailData = notificationClient.sendEmail(otpMailRequest) // open feign
        val mailData = notificationProducer.publish(otpMailRequest) // rabbitmq
        if (mailData is Error) return Error(mailData.failure)

        return Success(StatusResponse(EMAIL_VALIDATED.getMessage()))
    }
}
