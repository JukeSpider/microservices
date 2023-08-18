package com.juke.profileservice.feature.profile.domain.failure

import com.juke.profileservice.core.domain.failure.Failure

data class BadRequestFailure(
    override val code: Int = 400,
    override val status: String = "bad_request",
    override val message: String = "validation not passed",
) : Failure