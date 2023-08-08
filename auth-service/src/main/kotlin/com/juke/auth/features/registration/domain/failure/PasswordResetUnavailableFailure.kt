package com.juke.auth.features.registration.domain.failure

import com.juke.auth.core.domain.failure.Failure

data class PasswordResetUnavailableFailure (
    override val code: Int = 403,
    override val status: String = "forbidden",
    override val message: String = "password reset unavailable"
) : Failure