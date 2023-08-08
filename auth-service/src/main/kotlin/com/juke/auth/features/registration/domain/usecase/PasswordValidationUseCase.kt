package com.juke.auth.features.registration.domain.usecase

import com.juke.auth.core.data.entity.PasswordEntity
import com.juke.auth.core.data.entity.enums.PasswordStatusEnum.ACTIVE
import com.juke.auth.core.data.entity.enums.UserStatusEnum.VERIFIED
import com.juke.auth.core.data.service.PasswordService
import com.juke.auth.core.data.service.UserService
import com.juke.auth.core.domain.model.Data
import com.juke.auth.core.domain.model.Data.Error
import com.juke.auth.core.domain.model.value
import com.juke.auth.core.domain.success.StatusResponse
import com.juke.auth.core.domain.usecase.UseCase
import com.juke.auth.features.registration.data.entity.enums.FlowTypeEnum
import com.juke.auth.features.registration.data.entity.enums.FlowTypeEnum.REGISTRATION
import com.juke.auth.features.registration.data.entity.enums.FlowTypeEnum.UNDEFINED
import com.juke.auth.features.registration.data.entity.enums.OtpStatusEnum.CONFIRMED
import com.juke.auth.features.registration.data.entity.enums.OtpStatusEnum.REVOKED
import com.juke.auth.features.registration.data.service.OtpCodeService
import com.juke.auth.features.registration.domain.failure.InvalidPasswordFailure
import com.juke.auth.features.registration.domain.failure.UndefinedFlowFailure
import com.juke.auth.features.registration.domain.usecase.PasswordValidationUseCase.PasswordParams
import com.juke.auth.features.registration.domain.utils.EmailValidator
import com.juke.auth.features.registration.domain.utils.OtpCodeValidator
import com.juke.auth.features.registration.domain.utils.PasswordUtils
import com.juke.auth.features.registration.domain.utils.PasswordValidator
import com.juke.auth.features.registration.presentation.dto.enums.RegistrationStatusEnum.PASSWORD_VALIDATED
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class PasswordValidationUseCase(
    private val userService: UserService,
    private val otpService: OtpCodeService,
    private val passwordService: PasswordService,
    private val passwordUtils: PasswordUtils,
    private val passwordEncoder: PasswordEncoder,
    private val emailValidator: EmailValidator,
    private val otpCodeValidator: OtpCodeValidator,
    private val passwordValidator: PasswordValidator,
) : UseCase<PasswordParams, StatusResponse> {

    data class PasswordParams(
        val email: String,
        val code: String,
        val password: String,
        val flow: FlowTypeEnum,
    )

    override suspend fun invoke(params: PasswordParams): Data<StatusResponse> {
        if (params.flow == UNDEFINED) return Error(UndefinedFlowFailure())

        // validate password for pattern
        val validPassword = passwordUtils.validate(params.password)
        if (!validPassword) return Error(InvalidPasswordFailure())

        // check email
        val userData = emailValidator.validateEmail(params.email, params.flow)
        if (userData is Error) return Error(userData.failure)
        val user = userData.value!!

        // check otp code
        val otpCodeData = otpCodeValidator.validateOtpCode(
            code = params.code,
            userId = user.id!!,
            status = CONFIRMED,
            flow = params.flow,
        )
        if (otpCodeData is Error) return Error(otpCodeData.failure)
        val otpCode = otpCodeData.value!!

        // check password
        val passwordData = passwordValidator.validatePassword(params.password, user.id)
        if (passwordData is Error) return Error(passwordData.failure)

        // revoke passwords
        val passwordsRevokedData = passwordService.revokeAllUserPasswords(user.id)
        if (passwordsRevokedData is Error) return Error(passwordsRevokedData.failure)

        // revoke otp
        val updatedOtp = otpCode.copy(
            status = REVOKED,
            updatedAt = LocalDateTime.now(),
        )
        val otpSavedData = otpService.save(updatedOtp)
        if (otpSavedData is Error) return Error(otpSavedData.failure)

        // save password
        val password = PasswordEntity(
            pwd = passwordEncoder.encode(params.password),
            userId = user.id,
            status = ACTIVE
        )
        val passwordSavedData = passwordService.save(password)
        if (passwordSavedData is Error) return Error(passwordSavedData.failure)

        // update user
        val status = if (params.flow == REGISTRATION) VERIFIED else user.status
        val updatedUser = user.copy(status = status, updatedAt = LocalDateTime.now())

        val savedUpdatedUser = userService.save(updatedUser)
        if (savedUpdatedUser is Error) return Error(savedUpdatedUser.failure)

        return Data.Success(StatusResponse(PASSWORD_VALIDATED.getMessage()))
    }
}
