package com.juke.auth.features.registration.domain.failure

import com.juke.auth.core.domain.failure.Failure

data class OtpNotFoundFailure(
    override val code: Int = 400,
    override val status: String = "otp_not_found",
    override val message: String = "Otp code not found!"
) : Failure
