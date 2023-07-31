package com.juke.auth.features.authentication.domain.failure

import com.juke.auth.core.domain.failure.Failure

data class PasswordNotFoundFailure(
    override val code: Int = 400,
    override val status: String = "password_not_found",
    override val message: String = "Password not found!"
) : Failure
