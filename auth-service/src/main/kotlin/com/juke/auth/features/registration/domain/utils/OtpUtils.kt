package com.juke.auth.features.registration.domain.utils

import com.juke.auth.features.registration.config.properties.OtpProperties
import com.juke.auth.features.registration.data.entity.OtpCodeEntity
import com.juke.auth.features.registration.data.entity.enums.OtpStatusEnum
import org.springframework.stereotype.Component
import java.security.SecureRandom
import java.time.LocalDateTime
import java.util.*

@Component
class OtpUtils(
    private val otpProperties: OtpProperties,
) {

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
            expiresAt = LocalDateTime.now().plusMinutes(otpProperties.pending)
        )
    }

    fun isExpired(otpCode: OtpCodeEntity): Boolean {
        return otpCode.expiresAt.isBefore(LocalDateTime.now())
    }
}