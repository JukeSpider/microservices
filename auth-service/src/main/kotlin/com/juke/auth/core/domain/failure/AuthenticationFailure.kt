package com.juke.auth.core.domain.failure

data class AuthenticationFailure(
    override val code: Int = 401,
    override val status: String = "unauthorized",
    override val message: String = "Unauthorized!"
) : Failure