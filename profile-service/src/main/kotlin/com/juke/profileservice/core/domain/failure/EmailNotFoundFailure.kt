package com.juke.profileservice.core.domain.failure

data class EmailNotFoundFailure(
    override val code: Int = 400,
    override val status: String = "email_not_found",
    override val message: String = "Email not found!"
) : Failure
