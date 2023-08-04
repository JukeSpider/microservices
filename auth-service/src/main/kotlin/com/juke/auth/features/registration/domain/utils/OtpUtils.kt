package com.juke.auth.features.registration.domain.utils

import org.springframework.stereotype.Component
import java.security.SecureRandom

@Component
class OtpUtils {

    private val random: SecureRandom = SecureRandom()

    fun generateOtp(): String {
        val nums = random.nextInt(10000)
        return String.format("%4s", nums).replace(' ', '0')
    }
}