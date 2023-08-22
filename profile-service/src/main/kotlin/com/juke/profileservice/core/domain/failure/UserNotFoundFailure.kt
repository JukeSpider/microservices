package com.juke.profileservice.core.domain.failure

data class UserNotFoundFailure(
    override val code: Int = 400,
    override val status: String = "not_found",
    override val message: String = "user_not_found",
) : Failure
