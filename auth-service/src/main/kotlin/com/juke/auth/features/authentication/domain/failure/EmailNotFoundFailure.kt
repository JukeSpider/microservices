package com.juke.auth.features.authentication.domain.failure

import com.juke.auth.core.domain.failure.Failure

data class EmailNotFoundFailure(
    override val code: Int = 400,
    override val status: String = "email_not_found",
    override val message: String = "Email not found!"
) : Failure
