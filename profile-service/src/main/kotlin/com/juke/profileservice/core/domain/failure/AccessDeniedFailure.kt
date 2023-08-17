package com.juke.profileservice.core.domain.failure

data class AccessDeniedFailure(
    override val code: Int = 403,
    override val status: String = "access_denied",
    override val message: String = "Access Denied!"
) : Failure