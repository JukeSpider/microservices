package com.juke.profileservice.feature.jobposition.domain.failure

import com.juke.profileservice.core.domain.failure.Failure

data class InvalidIdFailure(
    override val code: Int = 403,
    override val status: String = "invalid_id",
    override val message: String = "invalid id",
) : Failure