package com.juke.profileservice.core.domain.failure

data class ExpiredTokenFailure(
    override val code: Int = 401,
    override val status: String = "expired_token",
    override val message: String = "Token is Expired!"
) : Failure