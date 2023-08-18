package com.juke.profileservice.core.domain.failure

data class JobPositionNotFoundFailure(
    override val code: Int = 400,
    override val status: String = "not_found",
    override val message: String = "job position not found"
): Failure