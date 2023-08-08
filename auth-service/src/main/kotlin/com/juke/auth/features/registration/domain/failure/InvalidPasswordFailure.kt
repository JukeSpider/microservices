package com.juke.auth.features.registration.domain.failure

import com.juke.auth.core.domain.failure.Failure

data class InvalidPasswordFailure(
    override val code: Int = 400,
    override val status: String = "invalid password",
    override val message: String = "password is not followed by pattern"
) : Failure