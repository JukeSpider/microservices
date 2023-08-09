package com.juke.auth.features.admin.domain.failure

import com.juke.auth.core.domain.failure.Failure

data class UserAlreadyExistsFailure(
    override val code: Int = 400,
    override val status: String = "user_already_exists",
    override val message: String = "user with provided email already exists"
) : Failure