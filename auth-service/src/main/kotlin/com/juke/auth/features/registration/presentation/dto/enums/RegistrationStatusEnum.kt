package com.juke.auth.features.registration.presentation.dto.enums

enum class RegistrationStatusEnum {
    EMAIL_VALIDATED,
    OTP_CODE_VALIDATED,
    PASSWORD_VALIDATED;

    fun getMessage() : String {
        if (this == EMAIL_VALIDATED) return "email available, code is sent"
        if (this == OTP_CODE_VALIDATED) return "code is confirmed, waiting for password"
        return "new password update completed"
    }
}