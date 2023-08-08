package com.juke.auth.features.registration.domain.usecase

import com.juke.auth.core.data.entity.PasswordEntity
import com.juke.auth.core.data.entity.enums.PasswordStatusEnum.ACTIVE
import com.juke.auth.core.data.entity.enums.UserStatusEnum
import com.juke.auth.core.data.entity.enums.UserStatusEnum.VERIFIED
import com.juke.auth.core.data.service.PasswordService
import com.juke.auth.core.data.service.UserService
import com.juke.auth.core.domain.model.Data
import com.juke.auth.core.domain.model.Data.Error
import com.juke.auth.core.domain.model.value
import com.juke.auth.core.domain.success.StatusResponse
import com.juke.auth.core.domain.usecase.UseCase
import com.juke.auth.features.authentication.domain.failure.EmailNotFoundFailure
import com.juke.auth.features.registration.data.entity.enums.OtpStatusEnum.CONFIRMED
import com.juke.auth.features.registration.data.entity.enums.OtpStatusEnum.REVOKED
import com.juke.auth.features.registration.data.service.OtpCodeService
import com.juke.auth.features.registration.domain.failure.InvalidPasswordFailure
import com.juke.auth.features.registration.domain.failure.OtpNotFoundFailure
import com.juke.auth.features.registration.domain.failure.RegistrationUnavailableFailure
import com.juke.auth.features.registration.domain.usecase.PasswordValidationUseCase.PasswordParams
import com.juke.auth.features.registration.domain.utils.OtpUtils
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
    private val passwordValidator: PasswordValidator,
    private val passwordEncoder: PasswordEncoder,
    private val otpUtils: OtpUtils,
) : UseCase<PasswordParams, StatusResponse> {

    data class PasswordParams(
        val email: String,
        val code: String,
        val password: String,
    )

    override suspend fun invoke(params: PasswordParams): Data<StatusResponse> {
        val validPassword = passwordValidator.validate(params.password)
        if (!validPassword) return Error(InvalidPasswordFailure())

        val userData = userService.findByEmail(params.email)
        if (userData is Error) {
            return when (userData.failure) {
                is EmailNotFoundFailure -> Error(RegistrationUnavailableFailure())
                else -> Error(userData.failure)
            }
        }

        val user = userData.value!!
        if (user.status != UserStatusEnum.CREATED) return Error(RegistrationUnavailableFailure())

        val otpCodeData = otpService.findByCodeAndUser(code = params.code, userId = user.id!!)
        if (otpCodeData is Error) {
            return when (otpCodeData.failure) {
                is OtpNotFoundFailure -> Error(RegistrationUnavailableFailure())
                else -> Error(otpCodeData.failure)
            }
        }

        val otpCode = otpCodeData.value!!
        if (otpCode.status != CONFIRMED) return Error(RegistrationUnavailableFailure())
        if (otpUtils.isExpired(otpCode)) return Error(RegistrationUnavailableFailure())

        val updatedOtp = otpCode.copy(
            status = REVOKED,
            updatedAt = LocalDateTime.now(),
        )

        val otpSavedData = otpService.save(updatedOtp)
        if (otpSavedData is Error) return Error(otpSavedData.failure)

        val password = PasswordEntity(
            pwd = passwordEncoder.encode(params.password),
            userId = user.id,
            status = ACTIVE
        )

        val passwordSavedData = passwordService.save(password)
        if (passwordSavedData is Error) return Error(passwordSavedData.failure)

        val updatedUser = user.copy(status = VERIFIED, updatedAt = LocalDateTime.now())
        val savedUpdatedUser = userService.save(updatedUser)
        if (savedUpdatedUser is Error) return Error(savedUpdatedUser.failure)

        return Data.Success(StatusResponse(PASSWORD_VALIDATED.getMessage()))
    }
}
