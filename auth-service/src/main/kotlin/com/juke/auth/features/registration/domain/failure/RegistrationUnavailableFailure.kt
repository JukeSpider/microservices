package com.juke.auth.features.registration.domain.failure

import com.juke.auth.core.domain.failure.Failure

data class RegistrationUnavailableFailure(
    override val code: Int = 403,
    override val status: String = "forbidden",
    override val message: String = "registration unavailable"
) : Failure