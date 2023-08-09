package com.juke.auth.features.registration.domain.failure

import com.juke.auth.core.domain.failure.Failure

data class PasswordAlreadyUsedFailure(
    override val code: Int = 400,
    override val status: String = "password_already_used",
    override val message: String = "provided password is already used before"
) : Failure