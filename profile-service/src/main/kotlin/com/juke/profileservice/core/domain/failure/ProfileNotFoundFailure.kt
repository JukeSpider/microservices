package com.juke.profileservice.core.domain.failure

data class ProfileNotFoundFailure(
    override val code: Int = 400,
    override val status: String = "not_found",
    override val message: String = "profile not found"
): Failure