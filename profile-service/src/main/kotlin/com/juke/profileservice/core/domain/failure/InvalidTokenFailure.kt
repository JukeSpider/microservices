package com.juke.profileservice.core.domain.failure

data class InvalidTokenFailure(
    override val code: Int = 400,
    override val status: String = "invalid_token",
    override val message: String = "Token is Invalid!"
) : Failure