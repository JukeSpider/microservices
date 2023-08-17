package com.juke.profileservice.core.domain.failure

data class PasswordNotFoundFailure(
    override val code: Int = 400,
    override val status: String = "password_not_found",
    override val message: String = "Password not found!"
) : Failure
