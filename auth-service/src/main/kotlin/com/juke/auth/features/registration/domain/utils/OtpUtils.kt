package com.juke.auth.features.registration.domain.utils

import com.juke.auth.features.registration.data.entity.OtpCodeEntity
import com.juke.auth.features.registration.data.entity.enums.OtpStatusEnum
import org.springframework.stereotype.Component
import java.security.SecureRandom
import java.time.LocalDateTime
import java.util.UUID

@Component
class OtpUtils {

    private val random: SecureRandom = SecureRandom()

    private fun generateOtp(): String {
        val nums = random.nextInt(10000)
        return String.format("%4s", nums).replace(' ', '0')
    }

    fun generateOtpCodeEntity(status: OtpStatusEnum, userId: UUID): OtpCodeEntity {
        return OtpCodeEntity(
            code = generateOtp(),
            status = status,
            userId = userId,
            expiresAt = LocalDateTime.now().plusMinutes(5)
        )
    }

    fun isExpired(otpCode: OtpCodeEntity): Boolean {
        return otpCode.expiresAt.isBefore(LocalDateTime.now())
    }
}