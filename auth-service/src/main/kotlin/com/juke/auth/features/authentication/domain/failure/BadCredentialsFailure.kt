package com.juke.auth.features.authentication.domain.failure

import com.juke.auth.core.domain.failure.Failure

data class BadCredentialsFailure(
    override val code: Int = 401,
    override val status: String = "email_or_password_incorrect",
    override val message: String = "Email or password incorrect!"
) : Failure
